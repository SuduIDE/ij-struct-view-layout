# Structure view customization

## General information

This plugin is designed to customize the structure view for existing languages and for use in new ones.

## Install

* Download LSV-plugin-*.zip from Release
* In intellij open __Settings__ &rarr; __Plugin__ &rarr; __Gear__ &rarr; __Install plugin from Disk__ and 
select the location of the downloaded plugin
* Reload Ide

## Usage

The customization of the structure view occurs with the help of json. To create it, you need to use the action "Create json SV". 
After executing this action, a ".customsv.json" file will be created. It will store structure view information for different languages.
In order to go to this file, you can find it in the file system or use the "Open json SV" action.
The json stores information about the structure view for each language. The general structure of json looks like this:

{ \
&nbsp; &nbsp; "Language name" : { \
&nbsp; &nbsp; &nbsp; &nbsp; "elements" : { \
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "class" : "CLASS_TOKEN_NAME", \
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "field" : "FIELD_TOKEN_NAME" \
&nbsp; &nbsp; &nbsp; &nbsp; } \
&nbsp; &nbsp; &nbsp; &nbsp; "attribute" : { \
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "type" : "TYPE_TOKEN_NAME", \
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "private" : "PRIVATE_TOKEN_NAME", \
&nbsp; &nbsp; &nbsp; &nbsp; } \
&nbsp; &nbsp; &nbsp; &nbsp; "filters" : { \
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "sort_names", "sort_privacy" \
&nbsp; &nbsp; &nbsp; &nbsp; } \
}

That is, for each language, a list of elements displayed in the structure view and a list of attributes are supported, 
allowing you to get information about the elements. \
Moreover, all these entities use the names of tokens from the AST-tree for a particular language. 
For example, in order to add the element "class" to the structure view for Java, 
you must specify the name of the token that corresponds to the class in Java - this is the token "CLASS"

You can add or remove filters, elements and their attributes, and when you save the file, 
the current state of the json will instantly affect the structure view

## Available features

All of the above actions are now available. And also support for some elements for Java is available, namely: classes, 
fields and methods, as well as many of their attributes, which are visually displayed in the structure view. 
Some filters have also been added: sorting by names, displaying by visibility, and displaying fields. \
At the moment, customization of filters and some basic elements and filters are not available for Java.
