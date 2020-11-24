package ipvc.estg.projeto.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.projeto.dao.TitleDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Title::class), version = 6, exportSchema = false)
public abstract class TitleDB : RoomDatabase() {

    abstract fun titleDao(): TitleDao

    private class WordDataBaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var titleDao = database.titleDao()

                    // Delete all content here.
                     //titleDao.deleteAll()

                    //Add sample words
                    /*var title = Title(1,"Segunda-Feira","Ir ao Ginásio","27/10/2020")
                    titleDao.insert(title)
                    var title1 = Title(2,"Terça-Feira","Lavar a cozinha","28/10/2020")
                    titleDao.insert(title1)
                    var title2 = Title(3,"Quarta-Feira","Estudar","29/10/2020")
                    titleDao.insert(title2)
                    var title3 = Title(4,"Quinta-Feira","Ir ao Ginásio","30/11/2020")
                    titleDao.insert(title3)
                    var title4 = Title(5,"Sexta-Feira","Ir às compras","12/11/2020")
                    titleDao.insert(title4)
                    var title5 = Title(6,"Sábado","Estudar","30/12/2020")
                    titleDao.insert(title5)
                    var title6 = Title(7,"Domingo","Estudar","14/12/2020")
                    titleDao.insert(title6)
                    var title7 = Title(8,"Segunda-Feira","Ir ao Ginásio","27/1/2020")
                    titleDao.insert(title7)
                    var title8 = Title(9,"Terça-Feira","Ir às compras","28/2/2020")
                    titleDao.insert(title8)
                    var title9 = Title(10,"Quarta-Feira","Lavar a cozinha","29/3/2020")
                    titleDao.insert(title9)
                    var title10 = Title(11,"Quinta-Feira","Ir às compras","30/4/2020")
                    titleDao.insert(title10)
                    var title11 = Title(12,"Sexta-Feira","Ir ao Ginásio","12/5/2020")
                    titleDao.insert(title11)
                    var title12 = Title(13,"Sábado","Estudar","30/8/2020")
                    titleDao.insert(title12)
                    var title13 = Title(14,"Domingo","Ir ao Ginásio","14/7/2020")
                    titleDao.insert(title13)*/
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TitleDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TitleDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TitleDB::class.java,
                        "titles_database",
                )
                        //estratégia de destrução
                        .fallbackToDestructiveMigration()
                        .addCallback(WordDataBaseCallback(scope))
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}