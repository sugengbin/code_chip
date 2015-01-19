1. StringUtils工具类（[org.apache.commons.lang.StringUtils][1]）
<font color='red'>StringUtils.Empty = '';</font> 
null-safe methods
 - <font color='red'>**isEmpty/isNotEmpty/isBlank/isNotBlank**</font> - checks if a String contains text
 - <font color='red'>**trim/strip**</font> - removes leading and trailing whitespace
 - **equals** - compares two strings null-safe
 - **startsWith/endsWith** - check if a String starts with a prefix/suffix null-safe
 - **indexOf/lastIndexOf/contains** - null-safe index-of checks
 - **indexOfAny/lastIndexOfAny/indexOfAnyBut/lastIndexOfAnyBut** - index-of any of a set of Strings
  - **containsOnly/containsNone/containsAny** - does String contains only/none/any of these characters
 - **substring/left/right/mid** - null-safe substring extractions
 - **substringBefore/substringAfter/substringBetween** - substring extraction relative to other strings
 - <font color='red'>**split/join**</font> - splits a String into an array of substrings and vice versa
 - **remove/delete** - removes part of a String
 - **replace/overlay** - Searches a String and replaces one String with another
 - **chomp/chop** - removes the last part of a String (chomp 移除换行符：A newline is "\n", "\r", or "\r\n".)
 - **leftPad/rightPad/center/repeat** - pads a String
 - <font color='red'>**upperCase/lowerCase/swapCase/capitalize/uncapitalize**</font> - changes the case of a String
 - **countMatches** - counts the number of occurrences of one String in another
 - **<font color='red'>isAlpha/isNumeric/isWhitespace/isAsciiPrintable**</font> - checks the characters in a String
 - **defaultString** - protects against a null input String
 - **reverse/reverseDelimited** - reverses a String （反序）
 - **abbreviate** - abbreviates a string using ellipsis (从第几个字节开始用省略号替换)
 - **difference** - compares Strings and reports on their differences 
 - **levensteinDistance** - the number of changes needed to change one String into another
 
  [1]: http://commons.apache.org/