package jp.kukv.portfolio.screens.contact

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ContactViewModel : ViewModel() {
    var firstName by mutableStateOf("")
        private set
    var lastName by mutableStateOf("")
        private set
    var company by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var message by mutableStateOf("")
        private set
    var agreedToPrivacyPolicy by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set

    private val emailRegex =
        Regex(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$",
        )

    val isFirstNameValid get() = firstName.isNotBlank() && firstName.length <= 100
    val isLastNameValid get() = lastName.isNotBlank() && lastName.length <= 100
    val isCompanyValid get() = company.isNotBlank() && company.length <= 100
    val isEmailValid get() = email.isNotBlank() && email.length <= 254 && emailRegex.matches(email)
    val isMessageValid get() = message.isNotBlank() && message.length <= 500

    val isFormValid
        get() =
            !isLoading &&
                isFirstNameValid &&
                isLastNameValid &&
                isCompanyValid &&
                isEmailValid &&
                isMessageValid &&
                agreedToPrivacyPolicy

    fun updateFirstName(value: String) {
        firstName = value
    }

    fun updateLastName(value: String) {
        lastName = value
    }

    fun updateCompany(value: String) {
        company = value
    }

    fun updateEmail(value: String) {
        email = value
    }

    fun updateMessage(value: String) {
        message = value
    }

    fun updateAgreedToPrivacyPolicy(value: Boolean) {
        agreedToPrivacyPolicy = value
    }

    fun submit(onSuccess: suspend () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            // TODO: バックエンド API への実際の送信処理に置き換える
            // TODO: ネットワークエラー・サーバーエラー時のエラーハンドリングを追加する
            delay(2000)
            isLoading = false
            onSuccess()
            resetForm()
        }
    }

    private fun resetForm() {
        firstName = ""
        lastName = ""
        company = ""
        email = ""
        message = ""
        agreedToPrivacyPolicy = false
    }
}
