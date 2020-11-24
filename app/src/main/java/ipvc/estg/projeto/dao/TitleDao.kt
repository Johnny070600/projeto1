package ipvc.estg.projeto.dao

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface  TitleDao {
    @Query("SELECT * from title_table ORDER BY title ASC")
    fun getAlphabetizedTitles(): LiveData<List<Title>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(title: Title)

    @Query("DELETE FROM title_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM title_table WHERE title == :title")
    fun getNotesByTitle(title: String): LiveData<List<Title>>

    @Query("DELETE FROM title_table where title == :title")
    suspend fun deleteByTitle(title: String)

    @Update
    suspend fun updateNote(title: Title)

    @Delete
    suspend fun deleteTitle(title: Title)

    @Query("SELECT * FROM title_table WHERE id=:title_id")
    suspend fun getTitle(title_id: Int) : List<Title>

    @Delete
    fun delete(title: Title?)
}