fun containsEven(collection: Collection<Int>): Boolean = collection.any {
    r -> r % 2 == 0
}
