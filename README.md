# belatrix_logger

Here is the tech test with some new refactors added.

1) Inside code_review, is the original .java file with the code review added.
2) This is the sql creation for the *new* Log_Values table : 
    create table log_values(
      message text NOT NULL,
      type int NOT NULL,
      created_at timestamp NOT NULL default now()
    );
