package com.regina.mgpa1.sections

import androidx.compose.runtime.Composable
import com.regina.mgpa1.logic.NavDestination

//==INTERFACES

//An interface that groups section-related composable methods to be implemented by
//the AppSection class
interface SectionImplementation {
    @Composable
    fun SectionLayout()
}

//==CLASSES

//An abstract class that serves as the base for each section class to inherit from
abstract class AppSection(
    val header: String
) : SectionImplementation {

}
