package ipvc.estg.projeto.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Musica_table")

class Musica (
    @PrimaryKey(autoGenerate = true) val id:Int? = null,
    @ColumnInfo(name = "Musica") val Musica: String,
    @ColumnInfo(name = "Banda") val Banda: String

)