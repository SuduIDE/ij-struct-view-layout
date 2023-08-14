# Structure view customization

_Name : Flexible Structure View_ \
_Version : 0.2.2_

## General information

This plugin is designed to customize the structure view for existing languages and for use in new ones. You can change 
the displayed elements, their text, icons and order.

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
* __filters__ - filters allow you to hide or display certain elements according to specified conditions, 
and you can also set sorting by specific keywords (for example, by visibility modifiers)

To get both the names of tokens and languages, you can use the PsiViewer plugin, it also allows you to define the hierarchy of elements in the language
#### Elements 

Each element must have a strict structure that allows it to be fully described for display in the structure view.
Namely, each element must have (or not) describe the field data:

* __"displayLevel"__ - defines the level in the hierarchy in the structure view relative to the parent.
    * -1 - do not display the element, while the children rise to its level
    * 0 - element not showing
    * 1 - element at the base level, the parent-child relationship does not change
    * \>1 - an element is moved up a given level in the hierarchy
* __"baseToken"__ -  AST-tree token in the given language, according to which the element will be created in the structure view
* __"attributes"__ - stores a list of attributes that the element contains. There can be two types:
  * __"set"__ - an implicit set when the language enumerates elements or attributes without explicitly identifying the list
  * __"unique"__ - contains unique attributes in the singular, such as a name, type, or other element
* __"baseIcon"__ - sets the base icon for this type of elements, if the conditions specified by the user are met, then an alternative icon will be displayed.
It has the following structure:
  * __"iconId"__ - id of the default icon set in attributes (see below)
  * __"attributeKey"__ - attribute of the element on which the condition will be checked. Optional parameter
  * __"attributeValue"__ - attribute values in the form of a list, if there is at least one match, then an alternative icon will be used. 
    If the specified attribute is another element, then the comparison will be based on the textual representation of this element. Optional parameter
  * __"alternativeIconId"__ - alternative icon id. Will be displayed only if the attribute is set and its value matches that of the element
* __"text"__ - sets the display text for elements. It is presented as a list, the elements of which are the specified attributes of the element. 
If the value of an element in the list is not an attribute, then it will be displayed as plain text. 
If the listed attributes are elements, then they will display their textual representation. 
Supported iterative text: "$i" - the number of the element in the parent
* __"description"__ - sets the display text for elements. Task method similar to __"text"__
* __"defaultAttributes"__ - default attributes, which may be different depending on the element's parents or children.
  This can be useful when the language has default properties that are assigned to different elements under certain conditions.
  For example, in Java, if you do not write any modifier before the class, then by default it will have package-private.
  These attributes can be used in displaying elements in a structure view, in filters or sorts. At the same time, they are used
  only last, unless otherwise stated. For example, if the specified default attribute has an icon, then it
  will be used only if there is no attribute with an icon of the same type in the standard attributes.
  * __parent__ - means that the listed attributes will depend on the parent. This is the attribute definition format
    such __"parentType" : "["List of keywords]"__, if you write instead of the parent __"else"__, then these attributes will be distributed
    with other parents.
  * __children__ - means that the listed attributes will depend on children. The way to set attributes is the same as for parent
#### Attributes

There are three types of attributes in total: __lists__, __properties__, __keywords__, __icons__. List and keywords have the following structure:

* __lists__ - contains attributes whose tokens are lists with other attributes or elements. 
For example, in Java this could be MODIFIER_LIST, which lists the modifier keywords.
* __properties__ - the text of the tokens is unique, such as the class name, etc. Consists of the following fields:
  * __"id"__ - property name, which may not depend on the language in any way: name, type, etc.
  * __"token"__ - the name of the token in the language being used
  * __"regexp"__ - an optional parameter containing a regular expression that allows you to use only those tokens whose text matches the expression
* __keywords__ - list of keywords specific to the language being used, such as the words class, public, etc. Consists of the following fields:
  * __"id"__ - the name of the token that will be used in the elements
  * __"token"__ - the name of the token used in the language
  * __"iconId"__ - icon id, allows to display elements with additional visual information. Not required parameter
  * __"sortValue"__ - value that can be used when sorting elements. Optional parameter
* __icons__ is a list of icons used in the language, each icon has the following structure:
  * __"id"__ - icon id, used in elements, keywords and filters
  * __"iconType"__ - icon type, allows you to specify how the icon is displayed in different ways. There are the following types:
    * __"Base"__ - base type, the icon is the main element when displayed. Used in baseIcon elements and filters
    * __"Offset"__ - the icon is displayed with a shift, most often used in keywords, allows you to display additional information
      next to the base icon
    * __"Mark"__ - also most often used in keywords, allows you to hang an additional mark on the main icon.
  * __"icon"__ - directly sets the icon with a keyword. There are three sources where an icon can be loaded from:
    * __"PlatformIcons"__ - to use an icon from PlatformIcons, just specify the name of the icon, which starts with a capital letter
    * __"AllIcons"__ - a list of all icons can be found [here](https://jetbrains.design/intellij/resources/icons_list/).
      Icons are contained in groups corresponding to their purpose. However, in order to get any icon, it is enough to specify
      her name, in which the first letter will be capitalized
    * __"UserPath"__ - full path to the icon, no larger than 48x48 px.

#### Filters

You can specify __sorts__ and __filters__ to reorder or filter elements.

Filters allow you to display only those elements that meet the specified conditions. Filters can have the following structure: \
* __"FilterName"__ - filter name given by the user, it is also displayed in the structure view:
  * __"elementType"__ - list of element types for which the filter will be applied.
  * __"notElementType"__ - list of element types for which the filter will not be applied.
  * __"attributeKey"__ - elements with this attribute will be filtered.
  * __"attributeValue"__ - elements whose key is equal to this value will be filtered. You can also specify default attributes here.
  * __"notAttributeValue"__ - elements whose value of this attribute is equal to this one will not take part in filtering. 
  You can also specify default attributes here.
  * __"iconId"__ - filter icon id displayed in structure view

Sorting allow you to order items by keywords, which have a value for sorting.
In general, sorts have the following structure:
* __"SortName"__ is the sort name given by the user, it is also displayed in the structure view:
  * __"sortBy"__ - keywords of elements to sort by. You can also specify default attributes here.
  * __"defaultValue"__ - default value, if there are no given keywords, then you can use it to determine the position
    items in sort
  * __"iconId"__ - sorting icon displayed in the structure view panel

#### Examples

Below is a simple example of a config for XML. For other examples see
[in examples](./examples/)

```json
{
  "JAVA": {
    "element": {
      "class": {
        "displayLevel": 1,
        "baseToken": "CLASS",
        "attributes": {
          "set": [],
          "unique": [
            "modifiers",
            "name",
            "class_keyword"
          ]
        },
        "baseIcon": {
          "iconId": "classIcon",
          "attributeKey": "modifiers",
          "attributeValue": [
            "abstract"
          ],
          "alternativeIconId": "abstractClassIcon"
        },
        "text": [
          "name"
        ],
        "description": [],
        "defaultAttributes" : {
          "parent" : {
            "else" : ["localKeyword"]
          }
        }
      }
    },
    "attribute": {
      "lists": {
        "modifiers": "MODIFIER_LIST",
        "parameters": "PARAMETER_LIST"
      },
      "properties": [
        {
          "id": "name",
          "token": "IDENTIFIER"
        }
      ],
      "keywords": [
        {
          "id": "class_keyword",
          "token": "CLASS_KEYWORD"
        },
        {
          "id": "private",
          "token": "PRIVATE_KEYWORD",
          "iconId": "private",
          "sortValue": 1
        },
        {
          "id": "public",
          "token": "PUBLIC_KEYWORD",
          "iconId": "public",
          "sortValue": 2
        },
        {
          "id": "final",
          "token": "FINAL_KEYWORD",
          "iconId": "final"
        },
        {
          "id": "static",
          "token": "STATIC_KEYWORD",
          "iconId": "static"
        },
        {
          "id": "abstract",
          "token": "ABSTRACT_KEYWORD"
        },
        {
          "id" : "localKeyword",
          "token" : "",
          "iconId" : "localIcon",
          "sortValue" : 2
        }
      ],
      "icons": [
        {
          "id": "classIcon",
          "iconType": "Base",
          "icon": "Class"
        },
        {
          "id": "abstractClassIcon",
          "iconType": "Base",
          "icon": "AbstractClass"
        },
        {
          "id": "interfaceIcon",
          "iconType": "Base",
          "icon": "Interface"
        },
        {
          "id": "field",
          "iconType": "Base",
          "icon": "Field"
        },
        {
          "id": "aClass",
          "iconType": "Base",
          "icon": "AnonymousClass"
        },
        {
          "id": "lambda",
          "iconType": "Base",
          "icon": "Lambda"
        },
        {
          "id": "methodIcon",
          "icon": "Method",
          "iconType": "Base"
        },
        {
          "id": "abstractMethodIcon",
          "iconType": "Base",
          "icon": "AbstractMethod"
        },
        {
          "id": "private",
          "icon": "Private",
          "iconType": "Offset"
        },
        {
          "id": "public",
          "icon": "Public",
          "iconType": "Offset"
        },
        {
          "id": "static",
          "icon": "StaticMark",
          "iconType": "Mark"
        },
        {
          "id": "final",
          "icon": "FinalMark",
          "iconType": "Mark"
        },
        {
          "id": "visibilitySort",
          "icon": "VisibilitySort",
          "iconType": "Base"
        }
      ]
    },
    "filters": {
      "visibility": {
        "Non-public": {
          "attributeKey": "modifiers",
          "attributeValue" : [
            "localKeyword"
          ],
          "notAttributeValue": [
            "public"
          ],
          "iconId": "private"
        }
      },
      "sorts" : {
        "visibility" : {
          "sortBy" : ["public", "private", "localKeyword"],
          "iconId" : "visibilitySort"
        }
      }
    }
  }
}
```