function (doc) {
    if (doc.class === "log")
    {
        emit(doc.id, doc);
    }
}