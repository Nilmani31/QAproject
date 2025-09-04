Feature: Task Management
  As a user
  I want to add, view, and delete tasks
  So that I can manage my work

  Scenario: User logs in and adds a task
    Given a user exists with username "chamsha" and password "1234"
    When the user logs in with username "chamsha" and password "1234"
    And adds a task with title "Buy milk" and description "From shop"
    Then the task "Buy milk" should appear in the task list
