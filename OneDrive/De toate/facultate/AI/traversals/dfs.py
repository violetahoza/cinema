from pyamaze import maze, agent, COLOR

def DFS(m): 
    start = (m.rows, m.cols) ## the starting point is the bottom-right corner
    explored = [start] ## a list to keep track of the cells that have been explored
    frontier = [start] ## a list that holds the cells to be explored
    dfsPath = {}
    while len(frontier) > 0:
        currCell = frontier.pop()
        if currCell == (1,1):
            break
        for direction in 'NESW':
            if m.maze_map[currCell][direction] == True:
                if direction == 'E':
                    child = (currCell[0], currCell[1] + 1)
                elif direction == 'W':
                    child = (currCell[0], currCell[1] - 1)
                elif direction == 'S':
                    child = (currCell[0] + 1, currCell[1])
                elif direction == 'N':
                    child = (currCell[0] - 1, currCell[1])
                if child in explored:
                    continue
                explored.append(child)
                frontier.append(child)
                dfsPath[child] = currCell
    fwdPath = {}
    cell = (1, 1)
    while cell != start:
        fwdPath[dfsPath[cell]] = cell
        cell = dfsPath[cell]
    return fwdPath

if __name__ == '__main__': 
    m = maze(15, 15)
    m.CreateMaze(loopPercent=100)
    path = DFS(m)
    a = agent(m, footprints=True)
    m.tracePath({a:path})
    m.run()