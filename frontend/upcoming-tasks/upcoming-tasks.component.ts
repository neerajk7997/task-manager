import { Component, OnInit } from '@angular/core';
import { TaskService } from '../task.service';
import { Task } from '../models/task.model';

@Component({
  selector: 'app-upcoming-tasks',
  templateUrl: './upcoming-tasks.component.html'
})
export class UpcomingTasksComponent implements OnInit {
  tasks: Task[] = [];
  selectedPriority: string = '';

  constructor(private taskService: TaskService) {}

  ngOnInit() {
    this.loadTasks();
  }

  loadTasks(priority?: string) {
    this.taskService.getTasks({ dueBefore: new Date().toISOString().split('T')[0], priority })
      .subscribe(data => this.tasks = data);
  }

  onPriorityChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedPriority = selectElement.value;
    this.loadTasks(this.selectedPriority || undefined);
  }
}
