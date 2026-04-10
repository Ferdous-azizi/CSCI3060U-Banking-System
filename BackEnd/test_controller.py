import unittest
from BackEndController import BackEndController
from Account import Account
from Transaction import Transaction

class TestControllerDecisionLoop(unittest.TestCase):

    def setUp(self):
        # Create a dummy controller for each test so we don't need real text files
        self.controller = BackEndController("dummy.txt", "dummy.txt", "dummy.txt")
        # Setup an existing account for testing decisions
        self.controller.accounts = {
            "10001": Account("10001", "Existing User", "A", 0.00, "NP", 0),
            "20002": Account("20002", "Rich User", "A", 500.00, "SP", 0)
        }

    # TC-04: Loop runs zero times
    def test_empty_loop(self):
        self.controller.transactions = []
        self.controller.processTransactions()
        self.assertEqual(len(self.controller.accounts), 2) # Nothing changed

    # TC-05: Create account (05)
    def test_create_account(self):
        self.controller.transactions = [
            Transaction("05", "New User", "30003", "100.00", "")
        ]
        self.controller.processTransactions()
        self.assertIn("30003", self.controller.accounts)
        self.assertEqual(self.controller.accounts["30003"].balance, 100.00)

    # TC-06: Ignore transaction for non-existent account
    def test_missing_account_ignored(self):
        self.controller.transactions = [
            Transaction("04", "Ghost", "99999", "50.00", "")
        ]
        self.controller.processTransactions()
        self.assertNotIn("99999", self.controller.accounts)

    # TC-07, 08, 09: Loop runs multiple times hitting various decisions
    def test_multiple_decisions(self):
        self.controller.transactions = [
            Transaction("04", "Rich User", "20002", "100.00", ""), # Hit 04 (Deposit)
            Transaction("06", "Existing User", "10001", "00.00", ""), # Hit 06 (Delete with 0 balance)
            Transaction("07", "Rich User", "20002", "00.00", ""), # Hit 07 (Disable)
            Transaction("08", "Rich User", "20002", "00.00", "")  # Hit 08 (Change Plan)
        ]
        self.controller.processTransactions()
        
        # Verify Delete worked
        self.assertNotIn("10001", self.controller.accounts)
        
        # Verify Deposit, Disable, and Plan Change worked
        rich_acc = self.controller.accounts["20002"]
        self.assertEqual(rich_acc.balance, 599.90) # 500 + 100 - 0.10 fee
        self.assertEqual(rich_acc.status, "D")
        self.assertEqual(rich_acc.plan, "NP")

if __name__ == '__main__':
    unittest.main()