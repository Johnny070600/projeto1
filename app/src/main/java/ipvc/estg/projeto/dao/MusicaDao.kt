package ipvc.estg.projeto.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipvc.estg.projeto.entities.Musica

@Dao

        interface MusicaDao {
            @Query("SELECT * FROM musica_table ORDER BY musica ASC")
            fun getAlphabetizedMusica(): LiveData<List<Musica>>

            @Insert(onConflict = OnConflictStrategy.IGNORE)
            suspend fun insert(musica: Musica)

            @Query("DELETE FROM musica_table")
            suspend fun deleteAll()
        }

