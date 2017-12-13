cv.dt <- c()
for (i in 1:5) {
	k <- 5*i
modelKNN <- CoreModel(formula, learn, model="knn", kInNN = k)
predKNN <- predict(modelKNN, test, type="class")
cv.dt[i] <- CA(test$PM10, predKNN)

}
cv.dt