package ipvc.estg.projeto.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.projeto.dao.MusicaDao
import ipvc.estg.projeto.entities.Musica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Musica::class), version = 6, exportSchema = false)
public abstract class MusicaDB : RoomDatabase() {

    abstract fun musicaDao(): MusicaDao

    private class WordDataBaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var titleDao = database.musicaDao()

                    // Delete all content here.
                    // titleDao.deleteAll()

                    //Add sample words
                    //var title = Title(1,"Segunda-Feira","Ir ao GinÃ¡sio","27/10/2020")
                    //titleDao.insert(title)
                    //title = Title(2,"TerÃ§a-Feira","Ir Ã s compras","28/10/2020")
                    //titleDao.insert(title)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MusicaDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MusicaDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicaDB::class.java,
                    "titles_database",
                )
                    //estratÃ©gia de destruÃ§Ã£o
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDataBaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}