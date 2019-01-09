-- Table Creation

create table users (
    user_id number(10) primary key,
    first_name varchar2(255) not null,
    last_name varchar2(255) not null,
    username varchar2(255) unique not null,
    user_password varchar2(255) not null
);

create table account (
    account_id number(10) primary key,
    user_id number(10) not null,
    balance decimal(20,2) default 0
);

create table transaction (
    transaction_id number(10) primary key,
    transaction_type varchar2(255) not null,
    account1 number(10) not null,
    account2 number(10),
    amount decimal(20,2) not null,
    transaction_time timestamp not null
);

-- Foreign Key Creation

alter table transaction add constraint trans_account_foreign_key
    foreign key (account1) references account (account_id) on delete cascade;
    
alter table account add constraint account_owner_foreign_key
    foreign key (user_id) references users (user_id) on delete cascade;
    
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
        insert into users
            (first_name, last_name, username, user_password, user_id)
            values
            (f_name, l_name, u_name, p_word, user_seq.nextval);
        u_id := user_seq.currval;
        commit;
    end;
/

create or replace procedure
    add_account(u_id number, a_id out number) as
    begin
        insert into account
            (account_id, user_id)
            values
            (account_seq.nextval, u_id);
        a_id := account_seq.currval;
        commit;
    end;
/

create or replace procedure
    make_deposit(a_id number, a decimal, t_id out number) as
    begin
        insert into transaction
            (account1, account2, amount, transaction_type, transaction_id, transaction_time)
            values
            (a_id, null, a, 'deposit   ', transaction_seq.nextval, localtimestamp);
        update account
            set balance = (select balance from account where account_id = a_id) + a where account_id = a_id;
        t_id := transaction_seq.currval;
        commit;
    end;
/
    
create or replace procedure
    make_withdrawal(a_id number, a decimal, t_id out number) as
    begin
        insert into transaction
            (account1, account2, amount, transaction_type, transaction_id, transaction_time)
            values
            (a_id, null, a, 'withdrawal', transaction_seq.nextval, localtimestamp);
        update account
            set balance = (select balance from account where account_id = a_id) - a where account_id = a_id;
        t_id := transaction_seq.currval;
        commit;
    end;
/

create or replace procedure
    issue_transfer(a_id1 number, a_id2 number, a decimal, t_id out number) as
    begin
        insert into transaction
            (account1, account2, amount, transaction_type, transaction_id, transaction_time)
            values
            (a_id1, a_id2, a, 'transfer  ', transaction_seq.nextval, localtimestamp);
        update account
            set balance = (select balance from account where account_id = a_id1) - a where account_id = a_id1;
        update account
            set balance = (select balance from account where account_id = a_id2) + a where account_id = a_id2;
        t_id := transaction_seq.currval;
        commit;
    end;
/

create or replace procedure
    remove_user(u_id number) as
    begin
        delete from users where user_id = u_id;
        commit;
    end;
/

create or replace procedure
    remove_account(a_id number) as
    begin
        delete from account where account_id = a_id;
        commit;
    end;
/

create or replace procedure
    update_user_fname(u_id number, f_name varchar2) as
    begin
        update users
            set first_name = f_name where user_id = u_id;
        commit;
    end;
/

create or replace procedure
    update_user_lname(u_id number, l_name varchar2) as
    begin
        update users
            set last_name = l_name where user_id = u_id;
        commit;
    end;
/

create or replace procedure
    update_user_uname(u_id number, uname varchar2) as
    begin
        update users
            set username = uname where user_id = u_id;
        commit;
    end;
/

create or replace procedure
    update_user_pass(u_id number, pass varchar2) as
    begin
        update users
            set user_password = pass where user_id = u_id;
        commit;
    end;
/

