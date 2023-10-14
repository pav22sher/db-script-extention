# IntelliJ IDEA Scripted Extension
![](imgs/img.png)

���������� ������� �� ������.
��������� ����� ���� Groovy � ����� ���-�� ������� � ��������� � �� �������.
Scripted Extension � ������� ������ Groovy, ��������� �� ������ �����. 
������ IDEA �� ���� ��� ����� ������������ ��� ������� ��� ��������� ���������� ����.

[src_database-openapi.zip](src_database-openapi.zip) - ���� ZIP-����� �������� �������� ��� ���������� OpenAPI.
��������� ��� ���������� ���������� �� ���������� ��������������� JavaDoc API, 
�������� OpenAPI �������� �������� ���������� ����������.

OpenAPI
---
### SchemaGeneratorBindings
```java
public final class SchemaGeneratorBindings {
    //������, ������� � ������ ������ ������
    public static final Binding<Project> PROJECT = new Binding<>("PROJECT");
    //��������� ��������, ��������� �������������
    public static final Binding<JBIterable<DasObject>> SELECTION = new Binding<>("SELECTION");
    //����� ������
    public static final Binding<Clipboard> CLIPBOARD = new Binding<>("CLIPBOARD");
    //������ �����
    public static final Binding<ScriptLogger> LOG = new Binding<>("LOG");
    //���������� ������
    public static final Binding<Files> FILES = new Binding<>("FILES");
}
```
### Log
�� ��������� LOG ���������� ��� � IntelliJ IDEA Log (Help|Open Log in Editor|idea.log).
```java
public interface ScriptLogger {
  void print(@NotNull String message);
  void error(@NotNull String message);
  void error(@NotNull String message, @Nullable Throwable th);
}
```
### Clipboard
```java
public interface Clipboard {
  String get();
  void set(String text);
}
```
### Files
```java
public interface Files {
  void chooseFileAndSave(String title, String description, Consumer<File> saveAction);
  void chooseDirectoryAndSave(String title, String description, Consumer<File> saveAction);
  void refresh(File file);
}
```
---
### DasUtil
����������� ������:  
getCatalog, getSchema, isPrimary, isForeign, hasAttribute, getPrimaryKey, getColumns � �.�.
### Das_
��� ������ ���� ������ ������������ com.intellij.database.model.Das_ ���������.

����� ������������ ����������:
* DasNamed - getName, getKind(SCHEMA, TABLE, COLUMN � �.�.)
* DasObject - getComment, getDasParent, getDasChildren
* DasSchemaChild - �������� ������� �����
* DasTable - �������
* DasTableChild - �������� ������� �������
* DasColumn - �������
* DasConstraint - �����������
* DasTableKey - ���� isPrimary
* DasForeignKey - ������� ���� getRefTableName


---
### Hello world
```groovy
//ru language is not support
//ru language is not support
FILES.chooseDirectoryAndSave(//Show window for select folder for saving
        "Export", //title
        "Export the results" //description
) { directory -> //saveAction
    def file = new File(directory, "results.txt") //save file results.txt in selected folder
    file << "Export completed!" //save file with this text
    LOG.print("Hello world!") //info
    LOG.error("Export completed!") //warn
}
```

### [aggregators](..%2F..%2FAppData%2FRoaming%2FJetBrains%2FIntelliJIdea2023.2%2Fextensions%2Fcom.intellij.database%2Fdata%2Faggregators)
![](imgs/aggregator.png)

### [extractors](..%2F..%2FAppData%2FRoaming%2FJetBrains%2FIntelliJIdea2023.2%2Fextensions%2Fcom.intellij.database%2Fdata%2Fextractors)
������� ������ �� � CSV, JSON � SQL Insert � �.�.
����� �������� ������ ��� �������� � ��������� ������.
```groovy
/*
 * Available context bindings:
 *   COLUMNS     List<DataColumn>
 *   ROWS        Iterable<DataRow>
 *   OUT         { append() }
 *   FORMATTER   { format(row, col); formatValue(Object, col); getTypeName(Object, col); isStringLiteral(Object, col); }
 *   TRANSPOSED  Boolean
 * plus ALL_COLUMNS, TABLE, DIALECT
 *
 * where:
 *   DataRow     { rowNumber(); first(); last(); data(): List<Object>; value(column): Object }
 *   DataColumn  { columnNumber(), name() }
 */
OUT.append("START")
OUT.append("\n")
COLUMNS.each { col ->
    OUT.append(col.name())
    OUT.append(", ")
}
OUT.append("\n")
ROWS.each { row ->
    COLUMNS.each { col ->
        OUT.append(FORMATTER.format(row, col))
        OUT.append(", ")
    }
    OUT.append("\n")
}
OUT.append("END")
```

### [schema](..%2F..%2FAppData%2FRoaming%2FJetBrains%2FIntelliJIdea2023.2%2Fextensions%2Fcom.intellij.database%2Fschema)
������� ��� �������������� ���������� ������� � DTO, Entity � �.�.

![](imgs/schema.png)

### �������:
[scripts](scripts)

### ������:
* https://github.com/novotnyr/mybatis-idea-scripted-extension
* https://programmer.group/5d6399803e956.html

### virtual columns and virtual views

![](imgs/virtual.png)

* https://www.jetbrains.com/help/idea/virtual-columns.html
* https://www.jetbrains.com/help/idea/virtual-views.html
