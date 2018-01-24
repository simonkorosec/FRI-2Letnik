source("RL2.R")

# set.seed(63160171)


#
# Lahko eksperimentirate z razlicnimi nastavitvami simuliranega okolja
#

# MAPWIDTH <- 50
# MAPHEIGHT <- 50
# NUMPREYS <- 10
# NUMPREDATORS <- 3


# za podan (nepopoln) primer v datoteki "RL.R"
# predstavlja vektor c(30, 4, 5) najvecje vrednosti istoleznih elementov
# v opisu stanja

qmat <- qlearning(c(3, 4, 5, 2, 4, 2, 4, 2, 4), maxtrials=5000)
		

## ORIGINAL 224
# qmat <- qlearning(c(30, 4, 5), maxtrials=500) #
# load(file="qmat_ORG.RData")


# save(qmat, file="qmat5000-RL2.RData")
# load(file="Q23000.RData")

# rm(.Random.seed, envir=globalenv()) # reset seed

# set.seed(123456789)
simulation(Q=qmat)

 # Za testeranje

v <- c()
rm(.Random.seed, envir=globalenv())
for (i in 1:100){
	print(i)
	flush.console()
	k <- simulation(Q=qmat)	
      v[i] <- k
}

mean(v)


