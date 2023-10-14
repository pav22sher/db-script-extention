import com.intellij.database.model.DasTable
import com.intellij.database.model.DasColumn
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import java.text.SimpleDateFormat

packageName = ""
typeMapping = [
        (~/(?i)bigint/)                            : "Long",
        (~/(?i)int|tinyint|smallint|mediumint/)    : "Integer",
        (~/(?i)bool|bit/)                          : "Boolean",
        (~/(?i)numeric|float|double|decimal|real/) : "BigDecimal",
        (~/(?i)datetime|timestamp/)                : "LocalDateTime",
        (~/(?i)date/)                              : "LocalDate",
        (~/(?i)time/)                              : "LocalTime",
        (~/(?i)blob|binary|bfile|clob|raw|image/)  : "byte[]",
        (~/(?i)/)                                  : "String"
]


FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}

def generate(table, dir) {
    def className = javaName(table.getName(), true)
    def fields = calcFields(table)
    packageName = getPackageName(dir)
    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, className + ".java")), "UTF-8"))
    printWriter.withPrintWriter { out -> generate(out, className, fields, table) }
}

// Get the folder path where the package is located
def getPackageName(dir) {
    return dir.toString()
            .replaceAll("\\\\", ".")
            .replaceAll("/", ".")
            .replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}

def generate(out, className, fields, table) {
    out.println "package $packageName"
    out.println ""
    out.println "import lombok.*;"
    out.println "import lombok.experimental.FieldDefaults;"
    out.println ""
    out.println "import javax.persistence.*;"
    out.println ""
    Set types = new HashSet()
    fields.each() {types.add(it.type)}
    if (types.contains("LocalDate")) out.println "import java.time.LocalDate;"
    if (types.contains("LocalTime")) out.println "import java.time.LocalTime;"
    if (types.contains("LocalDateTime")) out.println "import java.time.LocalDateTime;"
    if (types.contains("BigDecimal")) out.println "import java.math.BigDecimal;"

    out.println ""
    if (isNotEmpty(table.getComment())) {
        out.println "/**"
        out.println "* ${table.getComment().toString()}"
        out.println "*/"
    }
    out.println "@Data"
    out.println "@NoArgsConstructor"
    out.println "@FieldDefaults(level = AccessLevel.PRIVATE)"
    out.println "@Entity"
    out.println "@Table ( name =\"" + table.getName() + "\" , schema = \"" + DasUtil.getSchema(table) + "\")"
    out.println "public class $className {"
    fields.each() {
        if (isNotEmpty(it.comment)) {
            out.println "\t/**"
            out.println "\t * ${it.comment.toString()}"
            out.println "\t */"
        }
        out.println "\t${it.annotation}"
        out.println "\t${it.type} ${it.name};"
    }
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def colName = col.getName();
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def colType = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        def isPrimaryKey = DasUtil.hasAttribute(col, DasColumn.Attribute.PRIMARY_KEY);
        def field = [
                name         : javaName(colName, false),
                type         : colType,
                comment      : col.getComment(),
                annotation   : isPrimaryKey
                               ? "@Id\n\t@GeneratedValue\n\t@Column(name = \"" + colName + "\" )"
                               : "@Column(name = \"" + colName + "\" )"
        ]
        fields += [field]
    }
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

def isNotEmpty(content) {
    return content != null && content.toString().trim().length() > 0
}