package jp.kukv.portfolio.screens.showcase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ShowcaseViewModel : ViewModel() {
    var visibleCount by mutableStateOf(8)
    var selectedProject by mutableStateOf<Project?>(null)

    fun loadMore() {
        visibleCount += 8
    }

    fun selectProject(project: Project) {
        selectedProject = project
    }

    fun dismissProject() {
        selectedProject = null
    }
}
