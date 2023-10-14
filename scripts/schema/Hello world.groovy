//ru language is not support
FILES.chooseDirectoryAndSave(//Show window for select folder for saving
        "Export", //title
        "Export the results" //description
) { directory -> //saveAction
    def file = new File(directory, "results.txt") //save file results.txt in selected folder
    file << "Hello world! " //save file with this text
    LOG.print("Hello world!")
    LOG.error("Export completed!")
    CLIPBOARD.set("Export completed! ")
    file << CLIPBOARD.get()
}