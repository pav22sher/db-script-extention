values = new ArrayList<String>()
ROWS.each { row ->
  COLUMNS.each { column ->
    def value = row.value(column)
    values.add(value.toString())
  }
}
if (values.size() == 0) {
  OUT.append("Empty")
  return
}
OUT.append(String.join(",", values))