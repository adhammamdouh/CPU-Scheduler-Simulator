# CPU-Scheduler-Simulator
CPU Schedular Simulator: is a Project for Simulating the different Techniques CPU Handling Processes in the Ready Queue. Techniques Simulated (Shortest Job First, Shortest Remaining Time First, Priority Scheduling and AG)

1. Summary Describe each Technique.

   **1. SJR(Shortest Job First) :** CPU Takes take the Process with the Shortest Time(Burst) and Excute it. It could be preemptive where running process should be the one with shortest running time

**2. SRTF(Shortest Remaining Time First)**
  
CPU Takes take the Process with the Shortest Time(Burst) and Excute it. But the Difference Between it an SJF that the SRTF is Non-preemptive.

**3. Priority Scheduling**

CPU Takes take the Process with the highest Priority and Excute it. It is non Priority Technique.

**4. AG**

Here each Process has Arrival Time, Burst Time and Priority , Quantum and AG Factor. each process excution Time does not exceed it's Quantum.

AG Factor = Arrival Time + Burst Time + Priority. It is Non-preemptive Technique. CPU Takes the first Process in the ready queue, after half of Execution Time of the Current Process till end of it. CPU checks the ready if there ia any better Process "better Process means a Process with lower AG Factor". other wise continue excution when finishing, CPU takes first Process in the Ready Queue.

Required Libraries to build this Project:

- you need to download and import [JFreeChart Library](http://www.jfree.org/jfreechart/)
