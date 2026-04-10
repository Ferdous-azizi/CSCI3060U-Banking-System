import unittest
from Account import Account

class TestAccountStatement(unittest.TestCase):

    # TC-01: Testing a normal deposit to cover the 'else' fee logic
    def test_deposit_statement(self):
        acc = Account("11111", "Test User", "A", 100.00, "SP", 0)
        acc.deposit(50.00) 
        # 100 + (50 - 0.10) = 149.90
        self.assertEqual(acc.balance, 149.90)
        self.assertEqual(acc.total_transactions, 1)

    # TC-02: Testing a normal withdrawal to cover the 'if amount < 0' logic
    def test_withdraw_statement(self):
        acc = Account("11111", "Test User", "A", 100.00, "NP", 0)
        acc.withdraw(20.00)
        # 100 - (20 + 0.05) = 79.95
        self.assertAlmostEqual(acc.balance, 79.95)

    # TC-03: Testing failure to cover the 'else' (Error) branch
    def test_overdraft_statement(self):
        acc = Account("11111", "Test User", "A", 10.00, "SP", 0)
        acc.withdraw(50.00) # Should fail
        self.assertEqual(acc.balance, 10.00) # Balance should not change
        self.assertEqual(acc.total_transactions, 0)

if __name__ == '__main__':
    unittest.main()