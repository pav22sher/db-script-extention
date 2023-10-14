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