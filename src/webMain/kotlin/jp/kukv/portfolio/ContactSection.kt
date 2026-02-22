package jp.kukv.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSection(modifier: Modifier = Modifier) {
    val appState = LocalAppState.current
    val isMobile = appState.windowSize.isMobile
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var agreedToPrivacyPolicy by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val emailRegex =
        Regex(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$",
        )

    val isFirstNameValid = firstName.isNotBlank() && firstName.length <= 100
    val isLastNameValid = lastName.isNotBlank() && lastName.length <= 100
    val isCompanyValid = company.isNotBlank() && company.length <= 100
    val isEmailValid = email.isNotBlank() && email.length <= 254 && emailRegex.matches(email)
    val isMessageValid = message.isNotBlank() && message.length <= 500

    val isFormValid =
        !isLoading &&
            isFirstNameValid &&
            isLastNameValid &&
            isCompanyValid &&
            isEmailValid &&
            isMessageValid &&
            agreedToPrivacyPolicy

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "contact us",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Please fill out the form below to get in touch with us.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Surface(
            modifier = Modifier.widthIn(max = 600.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (isMobile) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First name*") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = firstName.isNotEmpty() && !isFirstNameValid,
                        supportingText = {
                            if (firstName.isNotEmpty() && !isFirstNameValid) {
                                Text(
                                    if (firstName.isBlank()) "Required" else "Max 100 characters",
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last name*") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = lastName.isNotEmpty() && !isLastNameValid,
                        supportingText = {
                            if (lastName.isNotEmpty() && !isLastNameValid) {
                                Text(
                                    if (lastName.isBlank()) "Required" else "Max 100 characters",
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text("First name*") },
                            modifier = Modifier.weight(1f),
                            isError = firstName.isNotEmpty() && !isFirstNameValid,
                            supportingText = {
                                if (firstName.isNotEmpty() && !isFirstNameValid) {
                                    Text(
                                        if (firstName.isBlank()) "Required" else "Max 100 characters",
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                }
                            },
                        )
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Last name*") },
                            modifier = Modifier.weight(1f),
                            isError = lastName.isNotEmpty() && !isLastNameValid,
                            supportingText = {
                                if (lastName.isNotEmpty() && !isLastNameValid) {
                                    Text(
                                        if (lastName.isBlank()) "Required" else "Max 100 characters",
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                }
                            },
                        )
                    }
                }

                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Company*") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = company.isNotEmpty() && !isCompanyValid,
                    supportingText = {
                        if (company.isNotEmpty() && !isCompanyValid) {
                            Text(
                                if (company.isBlank()) "Required" else "Max 100 characters",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email*") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = email.isNotEmpty() && !isEmailValid,
                    supportingText = {
                        if (email.isNotEmpty() && !isEmailValid) {
                            val errorText =
                                when {
                                    email.isBlank() -> "Required"
                                    email.length > 254 -> "Max 254 characters"
                                    !emailRegex.matches(email) -> "Invalid email format"
                                    else -> ""
                                }
                            Text(errorText, color = MaterialTheme.colorScheme.error)
                        }
                    },
                )

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("message*") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    isError = message.isNotEmpty() && !isMessageValid,
                    supportingText = {
                        if (message.isNotEmpty() && !isMessageValid) {
                            Text(
                                if (message.isBlank()) "Required" else "Max 500 characters",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Switch(
                        checked = agreedToPrivacyPolicy,
                        onCheckedChange = { agreedToPrivacyPolicy = it },
                    )
                    Text(
                        text = "By selecting this, you agree to our privacy policy.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            delay(2000)
                            isLoading = false
                            appState.navigation.snackbarHostState.showSnackbar("Message sent successfully!")
                            // Optional: Reset form
                            firstName = ""
                            lastName = ""
                            company = ""
                            email = ""
                            message = ""
                            agreedToPrivacyPolicy = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
