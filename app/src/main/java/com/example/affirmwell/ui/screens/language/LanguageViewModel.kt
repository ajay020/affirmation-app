import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.affirmwell.MainActivity
import com.example.affirmwell.utils.LocaleHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LanguageViewModel : ViewModel() {
    val languages = listOf(
        Language("English", "en"),
        Language("Hindi", "hi"),
        Language("German", "de")
    )
    private val _selectedLanguage = MutableStateFlow(languages[0])
    val selectedLanguage: StateFlow<Language> get() = _selectedLanguage

    fun selectLanguage(language: Language, context: Context) {
        _selectedLanguage.value = language
        LocaleHelper.setNewLocale(context, language.code)
//        restartApp(context)
    }

    fun loadSelectedLanguage(context: Context) {
        val selectedLanguageCode = LocaleHelper.getSelectedLanguage(context)
        _selectedLanguage.value = languages.find { it.code == selectedLanguageCode } ?: languages[0]
    }

    private fun restartApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }
}

data class Language(val name: String, val code: String)
