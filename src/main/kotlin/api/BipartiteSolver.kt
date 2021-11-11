package api

import computation.StepableComputation

interface BipartiteSolver<Worker, Job> :
    StepableComputation<BipartiteProblem<Worker, Job>, BipartiteSolution<Worker, Job>>