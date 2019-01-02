-- Table Creation

create table User (
    user_id number(10) primary key,
    first_name varchar2(255) not null,
    last_name varchar2(255) not null,
    username varchar2(255) unique not null,
    user_password varchar2(255) not null
);

create table Account (
    account_id number(10) primary key,
    user_id number(10) not null,
    balance binary_float default 0
);

create table Transaction (
    transaction_id number(10) primary key,
    transaction_type varchar2(255) not null,
    account1 number(10) not null,
    account2 number(10),
    amount binary_float not null,
    transaction_time timestamp not null
);

-- Foreign Key Creation

alter table transaction add constraint trans_account_foreign_key
    foreign key (account1) references account (account_id) on delete cascade;
    
alter table account add constraint account_owner_foreign_key
    foreign key (user_id) references user (user_id) on delete cascade;
    
-- Sequence Creation

create sequence user_seq
    start with 1
    increment by 1;
    
create sequence account_seq
    start with 1
    increment by 1;
    
create sequence transaction_seq
    start with 1
    increment by 1;
    
-- Stored Procedures Creation

create or replace procedure
    add_user(f_name varchar2, l_name varchar2, u_name varchar2, p_word varchar2, u_id out number) as
    begin
        insert into user
            (first_name, last_name, username, user_password, user_id)
            values
            (f_name, l_name, u_name, p_word, user_seq.nextval);
        u_id := user_seq.currval;
    end;

create or replace procedure
    add_account(u_id number, a_id out number) as
    begin
        insert into account
            (account_id, user_id)
            values
            (account_id, account_seq.nextval);
        u_id := account_seq.currval;
    end;

create or replace procedure
    make_deposit(a_id number, a binary_float, t_id out number) as
    begin
        insert into transaction
            (account1, account2, amount, transaction_type, transaction_id, transaction_time)
            values
            (a_id, null, a, 'deposit', transaction_seq.nextval, localtimestamp);
        update account
            set balance = (select balance from account where account_id = a_id) + a where account_id = a_id;
        t_id := transaction_seq.currval;
    end;
    
create or replace procedure
    make_withdrawal(a_id number, a binary_float, t_id out number) as
    begin
        insert into transaction
            (account1, account2, amount, transaction_type, transaction_id, transaction_time)
            values
            (a_id, null, a, 'withdrawal', transaction_seq.nextval, localtimestamp);
        update account
            set balance = (select balance from account where account_id = a_id) - a where account_id = a_id;
        t_id := transaction_seq.currval;
    end;

create or replace procedure
    issue_transfer(a_id1 varchar2, a_id2 number, a binary_float, t_id out number) as
    begin
        insert into transaction
            (account1, account2, amount, transaction_type, transaction_id, transaction_time)
            values
            (a_id1, a_id2, a, 'transfer', transaction_seq.nextval, localtimestamp);
        update account
            set balance = (select balance from account where account_id = a_id1) - a where account_id = a_id1;
        update account
            set balance = (select balance from account where account_id = a_id2) + a where account_id = a_id2;
        t_id := transaction_seq.currval;
    end;

-- Table Population



