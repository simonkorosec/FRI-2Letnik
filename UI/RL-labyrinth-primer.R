library(grid)

KEYPOS <- c(6,2)
EXITPOS <- c(4,10)

getLabyrinth <- function()
{
	lab <- matrix(c(
		1, 1, 1, 1, 1, 1, 1,
		1, 0, 0, 0, 0, 0, 1,
		1, 0, 1, 1, 1, 0, 1,
		1, 0, 1, 0, 0, 0, 1,
		1, 0, 1, 0, 1, 0, 1,
		1, 0, 1, 0, 1, 0, 1,
		1, 0, 1, 0, 1, 0, 1,
		1, 0, 0, 0, 0, 0, 1,
		1, 0, 1, 0, 1, 0, 1,
		1, 0, 0, 0, 0, 0, 1,
		1, 1, 1, 1, 1, 1, 1
	), nrow=11, byrow=T)

	sel <- lab == 1
	lab[sel] <- rgb(0, 0, 0)	# Wall		
	lab[!sel] <- rgb(1, 1, 1)	# Space
	lab
}

getNextPos <- function(lab, pos, dir)
{
	nextpos <- pos

	if (dir == 1) 									# North
		nextpos <- nextpos + c(0,-1)
	else if (dir == 2) 								# South
		nextpos <- nextpos + c(0,1)
	else if (dir == 3) 								# East
		nextpos <- nextpos + c(1,0)
	else if (dir == 4) 								# West
		nextpos <- nextpos + c(-1,0)
	else
		stop("unknown dir")

	if (lab[nextpos[2], nextpos[1]] == rgb(1, 1, 1))
		nextpos
	else
		pos				
}

selectFreeDirection <- function(lab, pos)
{
	dir <- vector()

	for (i in 1:4)
	{
		if (!identical(getNextPos(lab, pos, i), pos))
			dir <- c(dir, i)
	}

	sample(dir, 1, F)
}

getRandomPosition <- function(lab)
{
	pos <- vector()

	while(TRUE)
	{
		pos[1] <- sample(1:ncol(lab), 1, F)
		pos[2] <- sample(1:nrow(lab), 1, F)

		if (lab[pos[2], pos[1]] == rgb(1,1,1))
			return(pos)
	}
}

dim2sub <- function (iarr, dim) 
{
    iarr <- t(iarr)
    pdim <- c(1, cumprod(dim[-length(dim)]))
    iarr <- iarr - 1
    colSums(apply(iarr, 1, "*", pdim)) + 1
}

selectAction <- function(simData, Q)
{
	state <- getStateDesc(simData)
	
	len <- length(state)
	if (len > 1)
	{
		d <- dim(Q)
		n <- dim2sub(state, d[1:len])
	}
	else
	{
		n <- state[1]
	}

	vals <- apply(Q, len+1, '[', n)

	m <- max(vals)
	candidates <- which(vals==m)
	if (length(candidates) == 1)
		action <- candidates
	else
       	action <- sample(candidates,1)
	action
}

prepareSimData <- function()
{
	lab <- getLabyrinth()
	
	data <- list()	
	data$lab <- lab
	data$playerPos <- getRandomPosition(lab)
	data$playerDir <- NA
	data$playerKey <- FALSE

	data$playerCaught <- FALSE
	data$playerEscaped <- FALSE
	data$hunterPos <- getRandomPosition(lab)
	data$hunterDir <- NA
	
	data
}

simulationDraw <- function(simData)
{
	screen <- simData$lab
	screen[EXITPOS[2], EXITPOS[1]] <- rgb(0, 0, 1)					# Goal
	if (!simData$playerKey)
		screen[KEYPOS[2], KEYPOS[1]] <- rgb(1, 1, 0)				# Key
	screen[simData$playerPos[2], simData$playerPos[1]] <- rgb(0, 1, 0)	# Player
	screen[simData$hunterPos[2], simData$hunterPos[1]] <- rgb(1, 0, 0)	# Hunter

	grid.newpage()
	grid.raster(screen, interpolate=F)
}

simulationStep <- function(simData, Q=NULL)
{
	if (!is.null(Q))
		simData$playerDir <- selectAction(simData, Q)
	else if (is.na(simData$playerDir))
		simData$playerDir <- selectFreeDirection(simData$lab, simData$playerPos)
		
	nextPlayerPos <- getNextPos(simData$lab, simData$playerPos, simData$playerDir)
	if (identical(simData$playerPos, nextPlayerPos))
	{
		simData$playerDir <- NA
		nextPlayerPos <- simData$playerPos
	}

	if (is.na(simData$hunterDir))
		simData$hunterDir <- selectFreeDirection(simData$lab, simData$hunterPos)
		
	nextHunterPos <- getNextPos(simData$lab, simData$hunterPos, simData$hunterDir)
	if (identical(simData$hunterPos, nextHunterPos))
	{
		simData$hunterDir <- NA
		nextHunterPos <- simData$hunterPos
	}
		

	if (abs(nextPlayerPos[1] - nextHunterPos[1]) + abs(nextPlayerPos[2] - nextHunterPos[2]) <= 1)
		simData$playerCaught <- TRUE
	else if (nextPlayerPos[1] == EXITPOS[1] && nextPlayerPos[2] == EXITPOS[2] && simData$playerKey)
		simData$playerEscaped <- TRUE
	else if (nextPlayerPos[1] == KEYPOS[1] && nextPlayerPos[2] == KEYPOS[2])
		simData$playerKey <- TRUE

	simData$playerPos <- nextPlayerPos
	simData$hunterPos <- nextHunterPos

	simData
}

simulation <- function(Q=NULL, maxSteps=500)
{
	simData <- prepareSimData()
	
	steps=0
	while(!simData$playerCaught && !simData$playerEscaped && steps < maxSteps)
	{	
		simData <- simulationStep(simData, Q)
		
		simulationDraw(simData)

		steps <- steps + 1
		Sys.sleep(0.1)	
	}

	if (simData$playerEscaped)
		grid.text("WIN!", x=0.5, y=0.5, gp=gpar(fontsize=80, col="blue"))
	else if (simData$playerCaught)
		grid.text("CAUGHT!", x=0.5, y=0.5, gp=gpar(fontsize=80, col="red"))
}


qlearning <- function(dimStateSpace, gamma = 0.9, maxtrials = 1000, maxsteps = 500)
{
	dimQ <- c(dimStateSpace, 4)	# 4 actions (north, south, east, west)
	Q <- array(0, dim=dimQ)

	alpha <- 1
	ntrials <- 0
 
	while (alpha > 0.1 && ntrials < maxtrials)
	{
		simData <- prepareSimData()
		curState <- getStateDesc(simData)

		steps <- 0
		while (!simData$playerCaught && !simData$playerEscaped && steps < maxsteps)
		{
			simData$playerDir <- sample(1:4, 1, T) # 4 actions (north, south, east, west)                  

			simData <- simulationStep(simData)
			
			nextState <- getStateDesc(simData)
			reward <- getReward(curState, simData$playerDir, nextState)

			curStatePos <- dim2sub(c(curState, simData$playerDir), dimQ)
			len <- length(nextState)
			if (len > 1)
			{
				n <- dim2sub(nextState, dimStateSpace)
			}
			else
			{
				n <- nextState[1]
			}
			vals <- apply(Q, len+1, '[', n)			

              	Q[curStatePos] <- Q[curStatePos] + alpha * (reward + gamma * max(vals, na.rm = T) - Q[curStatePos])
              	curState <- nextState
			steps <- steps + 1
		}

		ntrials <- ntrials + 1
		alpha <- alpha * 0.9999

		print(paste("trial",ntrials,"out of max",maxtrials), quote=F)
		flush.console()
	}

	Q / max(Q)
}

getStateDesc <- function(simData)
{
	c(simData$playerPos[1], simData$playerPos[2], simData$hunterPos[1], simData$hunterPos[2], ifelse(simData$playerKey,2,1))
}

getReward <- function(oldstate, action, newstate)
{
	reward <- 0

	if (abs(newstate[1] - newstate[3]) + abs(newstate[2] - newstate[4]) <= 1)			# caught
		reward <- -1000
	else if (newstate[1] == KEYPOS[1] && newstate[2] == KEYPOS[2] && oldstate[5]==1)		# grab the key
		reward <- 1000
	else if (newstate[1] == EXITPOS[1] && newstate[2] == EXITPOS[2] && newstate[5]==2) 		# escape with the key
		reward <- 5000
	else if (newstate[1] == oldstate[1] && newstate[2] == oldstate[2])				# invalid move
		reward <- -1

	reward	
}



qmat <- qlearning(c(7, 11, 7, 11, 2), gamma=0.99, maxtrials=10000)
# save(qmat, file="qmat10000.RData")
# load(file="qmat10000.RData")

for (i in 1:100) 
{
	simulation(qmat)
	Sys.sleep(1)
}






