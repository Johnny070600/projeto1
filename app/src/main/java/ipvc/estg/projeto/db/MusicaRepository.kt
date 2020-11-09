package ipvc.estg.projeto.db

import androidx.lifecycle.LiveData
import ipvc.estg.projeto.dao.MusicaDao
import ipvc.estg.projeto.entities.Musica
import java.nio.file.Files.delete

class MusicaRepository(private val musicaDao: MusicaDao) {

    val allMusicas: LiveData<List<Musica>> = musicaDao.getAlphabetizedMusica()

    suspend fun insert(musica: Musica) {
        musicaDao.insert(musica)
    }


    fun delete(musica: Musica){
        musicaDao.delete(musica)
    }


    suspend fun deleteAll(){
        musicaDao.deleteAll()
    }
}