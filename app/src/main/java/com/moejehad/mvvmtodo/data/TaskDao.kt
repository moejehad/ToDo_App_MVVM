package com.moejehad.mvvmtodo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(query: String,sortOrder: SortOrder,hideComplete: Boolean) : Flow<List<Task>> =
        when(sortOrder){
            SortOrder.BY_DATE -> {
                getTasksSortedByDate(query,hideComplete)
            }
            SortOrder.BY_NAME -> {
                getTasksSortedByName(query,hideComplete)
            }
        }

    @Query("SELECT * FROM task_table WHERE (completed != :hideComplete OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC , name")
    fun getTasksSortedByName(searchQuery: String, hideComplete: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideComplete OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC , created")
    fun getTasksSortedByDate(searchQuery: String, hideComplete: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table WHERE completed = 1")
    suspend fun deleteCompletedTasks()
}