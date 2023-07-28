# Structure view customization

_Name : LSV_ \
_Version : 0.2_

## General information

This plugin is designed to customize the structure view for existing languages and for use in new ones.

## Install

* Download LSV-plugin-*.zip from Release
* In intellij open __Settings__ &rarr; __Plugin__ &rarr; __Gear__ &rarr; __Install plugin from Disk__ and 
select the location of the downloaded plugin
* Reload Ide

## Usage

The customization of the structure view occurs with the help of json. It allows you to set the elements that will be 
displayed in the structure, their attributes and visibility filters, which allow you to exclude the specified elements 
for one reason or another. Moreover, all this is available for any language.

### Creating a config file
First you need to create a json in which the structure view will be configured. This can be done with
action:
```
Create json SV
```
After that, the file ".customSV.json" will appear in the project folder, which will contain information about the structure view for Java.

### Opening the config

You can open the config in the usual way or using action:

```
Open json SV
```

After executing this action, the config will be automatically opened in the window

### Main points of customization

In the config at the top level is the language for which the structure view will be customized. 
Each language has a set of basic elements with which the structure view is customized. Namely:

* __elements__ - what will be displayed in the structure view (or not)
* __attributes__ - what elements consist of (names, modifiers, types, etc.)
* __filters__ - filters that allow you to exclude certain elements according to specified conditions

To get both the names of tokens and languages, you can use the PsiViewer plugin, it also allows you to define the hierarchy of elements in the language
#### Elements 

Each element must have a strict structure that allows it to be fully described for display in the structure view.
Namely, each element must have (or not) describe the field data:

* __"displayLevel"__ - defines the level in the hierarchy in the structure view relative to the parent.
    * 0 - element not showing
    * 1 - element at the base level, the parent-child relationship does not change
    * \>1 - an element is moved up a given level in the hierarchy
* __"baseToken"__ -  AST-tree token in the given language, according to which the element will be created in the structure view
* __"attributes"__ - stores a list of attributes that the element contains. There can be two types:
  * __"set"__ - an implicit set when the language enumerates elements or attributes without explicitly identifying the list
  * __"unique"__ - contains unique attributes in the singular, such as a name, type, or other element
* __"baseIcon"__ - allows you to set the main icon that will be displayed in the structure view for elements of this type. 
There is a set of standard icons, access to which is carried out using the default keyword, 
the icon is selected by the name of the element.
* __"text"__ - sets the display text for elements. It is presented as a list, the elements of which are the specified attributes of the element. 
If the value of an element in the list is not an attribute, then it will be displayed as plain text. 
If the listed attributes are elements, then they will display their textual representation. 
Supported iterative text: "$i" - the number of the element in the parent
* __"description"__ - sets the display text for elements. Task method similar to __"text"__

#### Attributes

There are three types of attributes in total: __list__, __properties__, __keywords__. Each of them is currently set in
this format:

```
"attribute name" : "the name of the token in the language being used"
```

* __list__ - contains attributes whose tokens are lists with other attributes or elements. 
For example, in Java this could be MODIFIER_LIST, which lists the modifier keywords.
* __properties__ - the text of the tokens is unique, such as the class name, etc.
* __keywords__ - keywords specific to the language being used, such as the words class, public, etc.

#### Filters

At the moment, only one type of filters is supported: visibility filters, they allow you to display certain elements according to specified conditions. 
Filters are specified using a name and conditions with the following structure:

* __"elementType"__ - list of element types for which the filter will be applied.
* __"notElementType"__ - list of element types for which the filter will not be applied.
* __"attributeKey"__ - elements with this attribute will be filtered.
* __"attributeValue"__ - elements whose key is equal to this value will be filtered.
* __"icon"__ - filter icon displayed in structure view

#### Examples

Below is a small example of a config for XML. For other examples see
[в примерах](./examples/)

```json
{
  "XML": {
    "element": {
      "tag": {
        "displayLevel": 1,
        "baseToken": "XML_TAG",
        "attributes": {
          "set": [
            "Attribute"
          ],
          "unique": [
            "name",
            "text"
          ]
        },
        "baseIcon": "default",
        "text": [
          "name"
        ],
        "description": [
          "Attribute"
        ]
      },
      "Attribute": {
        "displayLevel": 0,
        "baseToken": "XML_ATTRIBUTE",
        "attributes": {
          "set": [],
          "unique": [
            "name",
            "attribute_value"
          ]
        },
        "baseIcon": "",
        "text": [
          "name",
          "=",
          "attribute_value"
        ],
        "description": []
      }
    },
    "attribute": {
      "properties": {
        "name": "XML_NAME",
        "text": "XML_TEXT",
        "attribute_value": "XML_ATTRIBUTE_VALUE"
      }
    },
    "filters": {
      "visibility": {
        "Actions": {
          "attributeKey": "name",
          "attributeValue": "actions",
          "icon": "tag"
        }
      }
    }
  }
}
```