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

The config is a list of structure view settings for each. Each element contains a structure with the following parameters
at the top level

* __settings__ - contains a list of languages for which configuration will take place, as well as global structure view settings
* __elements__ - what will be displayed in the structure view (or not)
* __attributes__ - what elements consist of (names, modifiers, types, etc.)
* __filters__ - filters allow you to hide or display certain elements according to specified conditions, 
and you can also set sorting by specific keywords (for example, by visibility modifiers)

To get both the names of tokens and languages, you can use the PsiViewer plugin, it also allows you to define the hierarchy of elements in the language
Skeleton config with required top-level elements:
```json
[
  {
    "settings": {
    },
    "element": {
    },
    "attribute": {
    },
    "filters": {
    }
  }
]
```
#### Settings

This section contains a list of languages and settings that specify some behavior of the structure view 
(at the moment, you can set the file display):

```json
"settings": {
  "languages" : [
    //list of languages in any case.
  ],
  "showFile" : false or true
  // if showFile is false, then the file will not be displayed in the structure view
  // else will display the file name and its current directory
},
```

#### Elements 

Each element must have a strict structure that allows it to be fully described for display in the structure view.
Namely, each element must have (or not) describe the field data:

* __"displayLevel"__ - defines the level in the hierarchy in the structure view relative to the parent. 
Optional parameter, default value is 1.
    * -1 - do not display the element, while the children rise to its level
    * 0 - element not showing
    * 1 - element at the base level, the parent-child relationship does not change
    * \>1 - an element is moved up a given level in the hierarchy
* __"displayOnlyLevel"__ - if it is necessary to set the level only at which elements of this type will be displayed, then this parameter should be used. Optional parameter, can have the following values:
  * 0 - no specific level, items are displayed on all levels
  * \>0 - elements of this type are displayed only at the specified level
* __"baseToken"__ -  AST-tree token in the given language, according to which the element will be created in the structure view
* __"attributes"__ - stores a list of attributes that the element contains. These attributes can be used when displaying
  text, icons and element filtering. There are four types of attributes in total:
  * __"set"__ - an implicit set when the language enumerates elements or attributes without explicitly identifying the list
  * __"unique"__ - contains unique attributes in the singular, such as a name, type, or other element
  * __"optional"__ - contains a list of attributes that are not necessarily found on the element.
  * __"exclusive"__ - contains a list of attributes that should not be found on an element of this type. Used only when forming an element. 
If one of the attributes is found, the element will not be created
* __"baseIcon"__ - sets the base icon for this type of elements, if the conditions specified by the user are met, then an alternative icon will be displayed.
It has the following structure:
  * __"iconId"__ - id of the default icon set in attributes (see below). Required icon type - "Base"
  * __"attributeKey"__ - attribute of the element on which the condition will be checked. Optional parameter
  * __"attributeValue"__ - attribute values in the form of a list, if there is at least one match, then an alternative icon will be used. 
    If the specified attribute is another element, then the comparison will be based on the textual representation of this element. Optional parameter
  * __"alternativeIconId"__ - alternative icon id with type "Base". Will be used in the following cases:
    * If attributeKey is given and no list of attribute values is given, and attributeKey is present on the element
    * If attributeKey and attributeValue are given, and attributekey is contained in the element and its value is present in the list
      attributeValue
    * If the values from attributeValue are in the default attributes
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
  * __children__ - means that the listed attributes will depend on children. The way to set attributes is the same as for parent.
    You can also set default attributes if the element is a leaf. To do this, it is enough to set __"leaf"__ as the value of the child and list the corresponding attributes

Element structure example:

```json5
{
  "elementName": {
    "displayLevel": 1,
    //optional parameter
    "displayOnlyLevel": 0,
    "baseToken" : "ELEMENT_TOKEN",
    //optional parameter
    "attributes": {
      // optional attribute type 
      "set": [
        //list of attributes    
      ],
      // optional attribute type
      "unique": [
        //list of attributes
      ],
      // optional attribute type
      "optional": [
        //list of attributes
      ],
      "exclusive": [
        //list of attributes
      ] 
    },
    "baseIcon" : {
      "iconId": "iconId", // see below
      // optional
      "attributeKey" : "attributeName",
      // optional
      "attributeValue": [
        //possible attribute values
      ],
      "alternativeIconId" : "alternativeIconId"
    },
    "text" : [
      // attribute name or plain text
    ],
    "description" : [],
    "defaultAttributes" : {
      "parent" : {
        "parentElementType" : [
          //list of keywords
        ],
        "else" : []
      },
      "children" : {
        "childrenElementType" : [],
        "leaf" : [],
        "else" : []
      }
    }
  }
}
```
#### Attributes

There are three types of attributes in total: __lists__, __properties__, __keywords__, __icons__. List and keywords have the following structure:

* __lists__ - contains attributes whose tokens are lists with other attributes or elements. 
For example, in Java this could be MODIFIER_LIST, which lists the modifier keywords.
* __properties__ - the text of the tokens is unique, such as the class name, etc. Consists of the following fields:
  * __"id"__ - property name, which may not depend on the language in any way: name, type, etc.
  * __"token"__ - the name of the token in the language being used
  * __"regexp"__ - an optional parameter containing a regular expression that allows you to use only those tokens whose text matches the expression
  * __"notMatchedDisplayLevel"__ - display level of the element, if its property does not match. Доступные значения __0__ или __1__.
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
    * __"UserPath"__ - full or relative (project) pathto the icon, no larger than 48x48 px.

The general structure for setting attributes:
```json5
{
  "lists": {
    "listId": "TOKEN_LIST"
  },
  "properties": [
    {
      "id": "propertyId",
      "token": "PROPERTY_TOKEN",
      //optional
      "regexp": ".*",
      "notMatchedDisplayLevel": -1
    }
  ],
  "keywords": [
    {
      "id": "keywordId",
      "token": "KEYWORD_TOKEN",
      "iconId": "iconId",
      "sortValue": 1
    }
  ],
  "icons": [
    {
      "iconId": "iconId",
      "iconType": "Base",
      "icon": "Icon"
    }
  ]
}
```

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

The structure of filters and sorts:

```json5
{
  "visibility": {
    "FilterName": {
      "elementType" : [
        //list of elementTypes
      ],
      "notElementType": [
        //list of elementTypes
      ],
      "attributeKey": "attributesKeyName",
      "attributeValue": [
        //list of attribute values
      ],
      "iconId": "filterIconId"
    }
  },
  "sorts" : {
    "sortingName": {
      "sortBy": [
        // list of keywords
      ],
      "defaultValue" : 0,
      "iconId": "sortingIconId"
    }
  }
}
```

#### Examples

Below is a simple example of a config for YAML. For other examples see
[in examples](./examples/)

```json
[
  {
    "settings": {
      "languages": [
        "yaml"
      ],
      "showFile": true
    },
    "element": {
      "document" : {
        "baseToken": "Document ---",
        "attributes": {
        },
        "baseIcon": {
          "iconId": "tagIcon"
        },
        "text": [
          "YAML document"
        ],
        "description": []
      },
      "regular":  {
        "baseToken": "Key value pair",
        "attributes": {
          "optional": [
            "tag",
            "text"
          ],
          "unique": [
            "front_key"
          ]
        },
        "baseIcon": {
          "iconId": "tagIcon",
          "attributeKey" : "leafKeyword",
          "alternativeIconId" : "leafIcon"
        },
        "text": [
          "front_key"
        ],
        "description": [
          "tag",
          " ",
          "text"
        ],
        "defaultAttributes" : {
          "children" : {
            "leaf" : ["leafKeyword"]
          }
        }
      },
      "sequenceItem" : {
        "baseToken": "Sequence item",
        "attributes": {
          "exclusive" : ["text"]
        },
        "baseIcon": {
          "iconId": "tagIcon"
        },
        "text": [
          "Sequence Item"
        ],
        "description": []
      },
      "sequenceItemWithText" : {
        "baseToken": "Sequence item",
        "attributes": {
          "unique" : [
            "text"
          ]
        },
        "baseIcon": {
          "iconId": "leafIcon"
        },
        "text": [
          "text"
        ],
        "description": []
      }
    },
    "attribute": {
      "keywords" : [
        {
          "id" : "leafKeyword",
          "token" : ""
        }
      ],
      "properties": [
        {
          "id": "front_key",
          "token": "scalar key"
        },
        {
          "id": "text",
          "token": "text"
        },
        {
          "id": "tag",
          "token": "tag"
        }
      ],
      "icons": [
        {
          "id": "tagIcon",
          "iconType" : "Base",
          "icon" : "Tag"
        },
        {
          "id" : "leafIcon",
          "iconType": "Base",
          "icon" : "Property"
        }
      ]
    }
  }
]
```