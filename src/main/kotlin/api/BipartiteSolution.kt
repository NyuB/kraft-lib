package api

class BipartiteSolution<Worker, Job>(val matching: Map<Worker, Job>, val totalCost: Double)