import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.telegram.repository.TelegramBotMessageOnCommandAddRepository
import com.example.telegram.repository.TelegramBotMessageOnCommandPhotoRepository
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.data.repository.DataBaseRepositoryImpl
import com.example.telephoto.data.storage.client.models.ClientSQLite
import com.example.telephoto.data.storage.client.sqlite.SQLiteRepositoryImpl
import com.example.telephoto.data.storage.filesystem.usecase.GetOutputDirectoryUseCase
import com.example.telephoto.domain.models.Client
import com.example.telephoto.presentation.PhotoFragment
import java.io.File

class TelegramBotMessageRepositoryAppImpl: TelegramBotMessageOnCommandAddRepository, TelegramBotMessageOnCommandPhotoRepository {

    private val contextApp = TelegramBotApp.context
    private val dataBaseRepository by lazy { DataBaseRepositoryImpl(contextApp, null) }
    private val sqliteRepository by lazy{ SQLiteRepositoryImpl(context = contextApp, factory = null) }

    override fun noCommandAdd(client: Client): String {
        val clientSQLite = dataBaseRepository.clientToData(client)
        val clientSQL = sqliteRepository.getClientByNickname(ClientSQLite(nickname = "@${clientSQLite.nickname}"))

        if (clientSQL == null){
            return if (sqliteRepository.addClient(ClientSQLite(clientSQLite.chatId, clientSQLite.firstName ,clientSQLite.lastName, "@${clientSQLite.nickname}", true))){
                val getClient = sqliteRepository.getClientByNickname(ClientSQLite(nickname = "@${clientSQLite.nickname}"))
                if (getClient != null){
                    Handler(Looper.getMainLooper()).post {
                        tukTukClient.value = getClient
                    }
                    "Your request added!"
                }else{
                    "Repeat later!"
                }
            }else
                "Repeat later!"
        }else{
            return if(clientSQL.addStatus == true)
                "Your request has not yet been processed!"
            else
                "You`re already added!"
        }

    }

    companion object{

        val tukTukClient = MutableLiveData<ClientSQLite>()

    }

    override fun onCommandPhoto(
        client: Client,
        successCallback: (File) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        val clientSQLite = dataBaseRepository.clientToData(client)
        val getClient = sqliteRepository.getClientByNickname(ClientSQLite(nickname = "@${clientSQLite.nickname}"))
        if ( getClient != null) {
            if (!getClient.addStatus!!){
                try {
                    PhotoFragment().takePhoto(
                        {
                            successCallback(it)
                        },{
                            errorCallback(it)
                        }
                    )
                }catch (e: java.lang.Exception){
                    Log.i("Bot", e.message.toString())
                }
            }else{
                errorCallback("Wait for the administrator to add you!")
            }
        }else{
            errorCallback("It`s private chat!")
        }
    }
}