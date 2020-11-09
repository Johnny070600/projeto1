package ipvc.estg.projeto.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.projeto.dao.MusicaDao
import ipvc.estg.projeto.db.MusicaDB
import ipvc.estg.projeto.db.MusicaRepository
import ipvc.estg.projeto.entities.Musica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MusicaRepository
    val allTitles: LiveData<List<Musica>>

    init {
        val musicasDao = MusicaDB.getDatabase(application, viewModelScope).musicaDao()
        repository = MusicaRepository(musicasDao)
        allTitles = repository.allMusicas
    }

    fun insert(musica: Musica) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(musica)
    }
    fun delete(musica: Musica) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(musica)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }
}