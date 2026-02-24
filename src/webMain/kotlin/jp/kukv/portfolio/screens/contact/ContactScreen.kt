package jp.kukv.portfolio.screens.contact

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.kukv.portfolio.app.LocalAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    onShowSnackbar: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appViewModel = LocalAppViewModel.current
    val isMobile = appViewModel.windowSizeState.isMobile
    val viewModel: ContactViewModel = viewModel { ContactViewModel() }

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
                NameFieldRow(viewModel = viewModel, isMobile = isMobile)

                OutlinedTextField(
                    value = viewModel.company,
                    onValueChange = { viewModel.updateCompany(it) },
                    label = { Text("Company*") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = viewModel.company.isNotEmpty() && !viewModel.isCompanyValid,
                    supportingText = {
                        if (viewModel.company.isNotEmpty() && !viewModel.isCompanyValid) {
                            Text(
                                if (viewModel.company.isBlank()) "Required" else "Max 100 characters",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                )

                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = { Text("Email*") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = viewModel.email.isNotEmpty() && !viewModel.isEmailValid,
                    supportingText = {
                        if (viewModel.email.isNotEmpty() && !viewModel.isEmailValid) {
                            val errorText =
                                when {
                                    viewModel.email.isBlank() -> "Required"
                                    viewModel.email.length > 254 -> "Max 254 characters"
                                    else -> "Invalid email format"
                                }
                            Text(errorText, color = MaterialTheme.colorScheme.error)
                        }
                    },
                )

                OutlinedTextField(
                    value = viewModel.message,
                    onValueChange = { viewModel.updateMessage(it) },
                    label = { Text("Message*") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    isError = viewModel.message.isNotEmpty() && !viewModel.isMessageValid,
                    supportingText = {
                        if (viewModel.message.isNotEmpty() && !viewModel.isMessageValid) {
                            Text(
                                if (viewModel.message.isBlank()) "Required" else "Max 500 characters",
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
                        checked = viewModel.agreedToPrivacyPolicy,
                        onCheckedChange = { viewModel.updateAgreedToPrivacyPolicy(it) },
                    )
                    Text(
                        text = "By selecting this, you agree to our privacy policy.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Button(
                    onClick = {
                        viewModel.submit(
                            onSuccess = { onShowSnackbar("Message sent successfully!") },
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = viewModel.isFormValid,
                ) {
                    if (viewModel.isLoading) {
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

@Composable
private fun NameFieldRow(
    viewModel: ContactViewModel,
    isMobile: Boolean,
) {
    if (isMobile) {
        FirstNameField(viewModel = viewModel, modifier = Modifier.fillMaxWidth())
        LastNameField(viewModel = viewModel, modifier = Modifier.fillMaxWidth())
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FirstNameField(viewModel = viewModel, modifier = Modifier.weight(1f))
            LastNameField(viewModel = viewModel, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun FirstNameField(
    viewModel: ContactViewModel,
    modifier: Modifier,
) {
    OutlinedTextField(
        value = viewModel.firstName,
        onValueChange = { viewModel.updateFirstName(it) },
        label = { Text("First name*") },
        modifier = modifier,
        isError = viewModel.firstName.isNotEmpty() && !viewModel.isFirstNameValid,
        supportingText = {
            if (viewModel.firstName.isNotEmpty() && !viewModel.isFirstNameValid) {
                Text(
                    if (viewModel.firstName.isBlank()) "Required" else "Max 100 characters",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}

@Composable
private fun LastNameField(
    viewModel: ContactViewModel,
    modifier: Modifier,
) {
    OutlinedTextField(
        value = viewModel.lastName,
        onValueChange = { viewModel.updateLastName(it) },
        label = { Text("Last name*") },
        modifier = modifier,
        isError = viewModel.lastName.isNotEmpty() && !viewModel.isLastNameValid,
        supportingText = {
            if (viewModel.lastName.isNotEmpty() && !viewModel.isLastNameValid) {
                Text(
                    if (viewModel.lastName.isBlank()) "Required" else "Max 100 characters",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}
