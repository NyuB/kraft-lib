package api

import graph.Edge
import graph.FlowEdge
import graph.FlowGraph

class BipartiteProblem<Worker, Job>(val workers : Set<Worker>, val jobs : Set<Job>, val weights : Map<Worker, Map<Job, Double>>)