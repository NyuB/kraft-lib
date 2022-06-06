package insa.decaeste.kraft.api

import insa.decaeste.kraft.algorithms.StepableComputation

interface BipartiteSolver<Worker, Job> :
    StepableComputation<BipartiteProblem<Worker, Job>, BipartiteSolution<Worker, Job>>