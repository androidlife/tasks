package com.tasks.screen.tasks.transformer

import com.tasks.model.FeedItem
import com.tasks.model.Feeds
import com.tasks.model.Profile
import com.tasks.model.Task
import com.tasks.network.TaskService
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

/**
 * This class is generally responsible for providing chains for various API calls and used by
 * @see com.tasks.screen.tasks.ListModel
 * It requires urlBase which is
 * @see com.tasks.network.URL_BASE
 * so that we can concatenate the profile url with url base
 * It also requires tagProfileName which is
 * @see com.tasks.network.TAG_PROFILE_NAME
 * and tagTaskName which is
 * @see com.tasks.network.TAG_TASK_NAME
 * so that we can make necessary text replacement for
 * @see FeedItem.text
 * with fetched
 * @see Profile
 * and
 * @see Task
 */
class FeedsTransformer(private val urlBase: String, private val tagProfileName: String, private val tagTaskName: String) {

    /**
     * Here we have
     * @param feeds
     * which contains List<FeedItem>, Map<Profile,List<Int>>, Map<Task,List<Int>>
     * and we iterated, Map<Profile,List<Int>>, Map<Task,List<Int>> with their key values
     * and create a Single<Profile> or Single<Task> and add in the list
     * Then we merge those single so that we could get List<Profile> and List<Task>
     * once we call that singles
     * To do that we need help of
     * @see TaskService
     */
    fun combineFeedsWithRemoteCalls(feeds: Feeds, taskService: TaskService): Triple<Single<Feeds>, Single<List<Profile>>, Single<List<Task>>> {
        val profilesRequest = ArrayList<Single<Profile>>()
        feeds.profiles.keys.forEach { profile: Profile ->
            val profileFetchTask: Single<Profile> = taskService.getProfile(profile.id).onErrorReturn { t -> Profile.getEmpty(profile.id) }
            profilesRequest.add(profileFetchTask)
        }
        val tasksRequest = ArrayList<Single<Task>>()
        feeds.tasks.keys.forEach { task: Task ->
            val taskFetchTask: Single<Task> = taskService.getTask(task.id).onErrorReturn { t -> Task.getEmpty(task.id) }
            tasksRequest.add(taskFetchTask)
        }

        val profilesRequestCombined = Single.merge(profilesRequest).toList().subscribeOn(Schedulers.io())
        val tasksRequestCombined = Single.merge(tasksRequest).toList().subscribeOn(Schedulers.io())
        return Triple(Single.just(feeds), profilesRequestCombined, tasksRequestCombined)
    }

    /**
     * This is another chain, that comes after
     * @see combineFeedsWithRemoteCalls
     * where we simply zip all the remote calls
     * and once we receive all the results, we return
     * List<FeedItem>  which contains all the updated Profile and Task value
     */
    fun zipProfilesAndTasksCall(triple: Triple<Single<Feeds>, Single<List<Profile>>, Single<List<Task>>>): Single<List<FeedItem>> {
        return Single.zip(
                triple.first,
                triple.second,
                triple.third,
                Function3<Feeds, List<Profile>, List<Task>, List<FeedItem>>
                { feeds, profiles, tasks ->
                    profiles.forEach { profile ->
                        profile.profileUrl = urlBase.plus(profile.profileUrl)
                        feeds.profiles[profile]?.forEach {
                            feeds.feedItems[it].profile = profile
                            feeds.feedItems[it].text = feeds.feedItems[it].text.replace(tagProfileName, profile.firstName)
                        }

                    }
                    tasks.forEach { task ->
                        feeds.tasks[task]?.forEach {
                            feeds.feedItems[it].task = task
                            feeds.feedItems[it].text = feeds.feedItems[it].text.replace(tagTaskName, "\"".plus(task.name).plus("\""))
                        }
                    }
                    feeds.feedItems
                })
    }
}
