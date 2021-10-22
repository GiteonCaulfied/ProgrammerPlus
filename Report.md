# [Team Name] Report

The following is a report template to help your team successfully provide all the details necessary for your report in a structured and organised manner. Please give a straightforward and concise report that best demonstrates your project. Note that a good report will give a better impression of your project to the reviewers.

*Here are some tips to write a good report:*

* *Try to summarise and list the `bullet points` of your project as much as possible rather than give long, tedious paragraphs that mix up everything together.*

* *Try to create `diagrams` instead of text descriptions, which are more straightforward and explanatory.*

* *Try to make your report `well structured`, which is easier for the reviewers to capture the necessary information.*

*We give instructions enclosed in square brackets [...] and examples for each sections to demonstrate what are expected for your project report.*

*Please remove the instructions or examples in `italic` in your final report.*

## Table of Contents

1. [Team Members and Roles](#team-members-and-roles)
2. [Conflict Resolution Protocol](#conflict-resolution-protocol)
2. [Application Description](#application-description)
3. [Application UML](#application-uml)
3. [Application Design and Decisions](#application-design-and-decisions)
4. [Summary of Known Errors and Bugs](#summary-of-known-errors-and-bugs)
5. [Testing Summary](#testing-summary)
6. [Implemented Features](#implemented-features)
7. [Team Meetings](#team-meetings)

## Team Members and Roles

| UID | Name | Role |
| :--- | :----: | ---: |
| u7212335 | Qinyu Zhao | Member |
| [uid] | Xiangyu Hui | Member |
| [uid] | Xuzeng He | Member |
| [uid] | Yikai Ge | Member |

## Conflict Resolution Protocol

*[Write a well defined protocol your team can use to handle conflicts. That is, if your group has problems, what is the procedure for reaching consensus or solving a problem? (If you choose to make this an external document, link to it here)]*

## Application Description

*[What is your application, what does it do? Include photos or diagrams if necessary]*

*Here is a pet specific social media application example*

*PetBook is a social media application specifically targetting pet owners... it provides... certified practitioners, such as veterians are indicated by a label next to their profile...*

**Application Use Cases and or Examples**

*[Provide use cases and examples of people using your application. Who are the target users of your application? How do the users use your application?]*

*Here is a pet training application example*

*Molly wants to inquiry about her cat, McPurr's recent troublesome behaviour*
1. *Molly notices that McPurr has been hostile since...*
2. *She makes a post about... with the tag...*
3. *Lachlan, a vet, writes a reply to Molly's post...*
4. ...
5. *Molly gives Lachlan's reply a 'tick' response*

*Here is a map navigation application example*

*Targets Users: Drivers*

* *Users can use it to navigate in order to reach the destinations.*
* *Users can learn the traffic conditions*
* ...

*Target Users: Those who want to find some good restaurants*

* *Users can find nearby restaurants and the application can give recommendations*
* ...

*List all the use cases in text descriptions or create use case diagrams. Please refer to https://www.visual-paradigm.com/guide/uml-unified-modeling-language/what-is-use-case-diagram/ for use case diagram.*

## Application UML

![ClassDiagramExample](./images/ClassDiagramExample.png)
*[Replace the above with a class diagram. You can look at how we have linked an image here as an example of how you can do it too.]*

## Application Design and Decisions

*Please give clear and concise descriptions for each subsections of this part. It would be better to list all the concrete items for each subsection and give no more than `5` concise, crucial reasons of your design. Here is an example for the subsection `Data Structures`:*

*I used the following data structures in my project:*

1. *LinkedList*

   * *Objective: It is used for storing xxxx for xxx feature.*

   * *Locations: line xxx in XXX.java, ..., etc.*

   * *Reasons:*

     * *It is more efficient than Arraylist for insertion with a time complexity O(1)*

     * *We don't need to access the item by index for this feature*

2. ...

3. ...

**Data Structures**

Our team use the AVL-Tree in our project.

- Objective: 

  - It is used for storing the swearwords for  surprise feature(iii) removing/hiding hate speech in posts;

  - We will search the stored swearwords to find out whether the target word is a swearword.

- Locations: 

  - Declared in *DataStructure\AVLTree.java* and used in *SwearWordsDAO.java* line *9*. 
  - Used for removing hate speech in line *52* in *HateSpeechParser\Tokenizer.java*
  - Persistent data in *app\src\main\assets\swear_words.json*

- Reasons:

  - Tree is easy to use *Gson* to read and store as persistent data(.json).
  - Compared with binary search tree AVL-Tree is close to balance tree, for look up with a time complexity *O(log(n))*
  - Compared with Red-Black Tree, the height of an AVL tree is bounded by roughly 1.44 * log2(n), while the height of  a red-black tree may be up to 2 * log2(n). Thus, lookup is slightly faster on the  average in AVL.

*[What data structures did your team utilise? Where and why?]*

**Design Patterns**

*[What design patterns did your team utilise? Where and why?]*

**Grammars**

- *The grammar rule*

  - Operators: 

    1. '&' represent take an intersection between two expressions

    2. '|' represent take a union between two expressions
    3. '=' used for conditional filtering 
    4. '(' and ')' used for applying precedence

  - Key

    1. Author
    2. Tag
    3. Title
    4. Id 

  - notification

    Our grammar is case sensitive, and spaces are not allowed in our grammar, as the authors' name and titles may contains space.  

  - Example

    A correct syntax will looks like ((Title=A|Tag=B)&Author=C)|Id=123

- *Search Engine*

Our search engine supports both intersection and union queries, and use () to apply precedence; At present most search engine only support intersection queries but not union quires like Firebase. 

For example, in our search engine we can do the following query:

```
(Tag=BotTalk|Tag=ANU)&Author=Qinyu Zhao
```

 This query will return all Qinyu Zhao's posts with tag containing BotTalk or with tag containing ANU

![App snapshot1](E:\Comp2100-6442\GroupAss\image-20211021211723190.png)

Our search engine also support partially valid and invalid search queries, when encounters an invalid section, it will toast a message indicating which section is invalid. After that, the search engine will search the neighbor section, and if the current section is a intersection query it will be casted to union query. For example, if we search in this way "TTTT=aaa&Author=Qinyu Zhao" , the result will be all the posts that written by QInyu Zhao, even though there is a & in the syntax.

![App snapshot2](E:\Comp2100-6442\GroupAss\image-20211021215235048.png)



*[How do you design the grammar? What are the advantages of your designs?]*

*If there are several grammars, list them all under this section and what they relate to.*

**Tokenizer and Parsers**

- Tokenizer and Parsers used in search engine

  - Tokenizer

    Token: 

     Keywords : [Title, Author, Tag, Id]

     Punctuators : [LBRA, RBRA]

    [Illegal] represent the current token is an invalid syntax for search

  - Parser 

    1. Parser grammar

    ```
    <exp>    ::= <term> | <term>&<exp> | <term>|<exp>
    <term>   ::=  <Post_List with key> | ( <exp> )
    ```

    2.  Node classes 

       public abstract class **Exp** 

       public class **KeyExp** extends **Exp** with a Post_List which satisfied the key. 

       public class **AndExp** extends **Exp **with Exp attributes **term** and **exp**

       public class **OrExp** extends **Exp**  with Exp attributes **term** and **exp**

  - How does search parser work

    1st. We need to pass the query string in our Tokenizer.

    2nd. Pass the tokenizer into Parser, inside the parser, it will pop out the tokens and left recursively create expressions.

    3rd. After building up our final expression, we can use evaluate() to recursively get the result, in the AndExp the evaluate function will return the intersection of the term's list and exp's list, OrExp will return the union of these two list, KeyExp is the base case will just return the local list. 

  - Derivation for expression building 

    - Title=A|Tag=B&Author=C|Id=123

      ![derivation chart](E:\Comp2100-6442\GroupAss\image-20211022010726837.png)

  - Derivation for expression executing 

    - Title=A|Tag=B&Author=C|Id=123

    ![logic flow chart](E:\Comp2100-6442\GroupAss\image-20211022011019037.png)

*[Where do you use tokenisers and parsers? How are they built? What are the advantages of the designs?]*

**Surpise Item**

*[If you implement the surprise item, explain how your solution addresses the surprise task. What decisions do your team make in addressing the problem?]*

**Other**

*[What other design decisions have you made which you feel are relevant? Feel free to separate these into their own subheadings.]*

## Summary of Known Errors and Bugs

*[Where are the known errors and bugs? What consequences might they lead to?]*

*Here is an example:*

1. *Bug 1:*

- *A space bar (' ') in the sign in email will crash the application.*
- ... 

2. *Bug 2:*
3. ...

*List all the known errors and bugs here. If we find bugs/errors that your team do not know of, it shows that your testing is not through.*

## Testing Summary

*[What features have you tested? What is your testing coverage?]*

*Here is an example:*

*Number of test cases: ...*

*Code coverage: ...*

*Types of tests created: ...*

*Please provide some screenshots of your testing summary, showing the achieved testing coverage. Feel free to provide further details on your tests.*

## Implemented Features

*[What features have you implemented?]*

*Here is an example:*

*User Privacy*

1. *Friendship. Users may send friend requests which are then accepted or denied. (easy)*
2. *Privacy I: A user must approve a friend's request based on privacy settings. (easy)*
3. *Privacy II: A user can only see a profile that is Public (consider that there are at least two types of profiles: public and private). (easy)*
4. *Privacy III: A user can only follow someone who shares at least one mutual friend based on privacy settings. (Medium)*

*Firebase Integration*
1. *Use Firebase to implement user Authentication/Authorisation. (easy)*
2. *Use Firebase to persist all data used in your app (this item replace the requirement to retrieve data from a local file) (medium)*

*List all features you have completed in their separate categories with their difficulty classification. If they are features that are suggested and approved, please state this somewhere as well.*

## Team Meetings

*Here is an example:*

- *[Team Meeting 1](Meeting1.md)*
- ...

*Either write your meeting minutes here or link to documents that contain them. There must be at least 3 team meetings.*