# ij-struct-view-layout
Layout for the Structure view in IntelliJ CE

## Task
Structure view is one of the most-used in the IDE. Tuning it for the specific language or sub-language usually requires quite a bit of manual work by implementing a structure view adapter. But in reality it filters out the language tree and changes presentation a bit. For some simple cases it's enough to specify custom icons and the list of language nodes to display in the IDE. It means that one can try writing some, let's say JSON config that IDE saves to project settings and uses for enhanced displaying of various small sublangs or even big languages. Matching could be performed by, for example, class names, or code construct types. Benefit for the student: deep dive into the code model API, useful plugin as a result. At the same time, this task could be reduced to make it more simple, or made more complex to provide more customization.
