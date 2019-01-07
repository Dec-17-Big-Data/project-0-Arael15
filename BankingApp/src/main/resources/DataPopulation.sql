-- Table Population

variable id number;

exec add_user('Jotaro', 'Kujo', 'StarPlatinum', 'HesOnly17', :id);
exec add_user('Joseph', 'Joestar', 'HermitPurple', 'OhMyGod', :id);
exec add_user('Noriaki', 'Kakyoin', 'HierophantGreen', 'EmeraldSplash', :id);
exec add_user('Mohammed', 'Avdol', 'MagiciansRed', 'YesIAm', :id);
exec add_user('Jean-Pierre', 'Polnareff', 'SilverChariot', 'NowJustATurtle', :id);
exec add_user('Dio', 'Brando', 'ZaWarudo', 'RoadRollerDa', :id);

exec add_account(1, :id);
exec add_account(1, :id);
exec add_account(2, :id);
exec add_account(2, :id);
exec add_account(3, :id);
exec add_account(3, :id);
exec add_account(4, :id);
exec add_account(4, :id);
exec add_account(5, :id);
exec add_account(5, :id);
exec add_account(6, :id);
exec add_account(6, :id);

exec make_deposit(1, 500.00, :id);
exec make_deposit(2, 500.00, :id);
exec make_deposit(3, 500.00, :id);
exec make_deposit(4, 500.00, :id);
exec make_deposit(5, 500.00, :id);
exec make_deposit(6, 500.00, :id);
exec make_deposit(7, 500.00, :id);
exec make_deposit(8, 500.00, :id);
exec make_deposit(10, 500.00, :id);
exec make_deposit(11, 500.00, :id);
exec make_deposit(12, 500.00, :id);

exec make_withdrawal(3, 200.00, :id);
exec make_withdrawal(3, 200.00, :id);
exec make_withdrawal(5, 100.00, :id);
exec make_withdrawal(6, 100.00, :id);
exec make_withdrawal(7, 10.00, :id);
exec make_withdrawal(7, 10.00, :id);
exec make_withdrawal(10, 300.19, :id);

exec issue_transfer(1, 2, 500.00, :id);
exec issue_transfer(2, 1, 1000.00, :id);
exec issue_transfer(4, 3, 200.00, :id);
exec issue_transfer(4, 3, 100.00, :id);
exec issue_transfer(7, 8, 111.11, :id);
exec issue_transfer(11, 12, 123.00, :id);
exec issue_transfer(11, 12, 45.67, :id);
exec issue_transfer(11, 12, 89.10, :id);
