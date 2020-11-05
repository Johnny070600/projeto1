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
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allTitles: LiveData<List<Musica>>

    init {
        val musicasDao = MusicaDB.getDatabase(application, viewModelScope).musicaDao()
        repository = MusicaRepository(musicasDao)
        allTitles = repository.allMusicas
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(musica: Musica) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(musica)
    }

    //delete All
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }
}