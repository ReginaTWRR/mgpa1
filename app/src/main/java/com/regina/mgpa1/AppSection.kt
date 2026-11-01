package com.regina.mgpa1

import androidx.compose.runtime.Composable

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
