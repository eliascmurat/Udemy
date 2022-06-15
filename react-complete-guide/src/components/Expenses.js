import './Expenses.css';
import ExpenseItem from './ExpenseItem';

function Expenses(props) {  
  return (
    <div className='expenses'>
      { 
        props.expenses.map(expense =>
          <ExpenseItem
            key={ expense.id }
            title={ expense.title }
            amount={ expense.amount }
            date={ expense.date }
          />
        ) 
      }
    </div>
  );
}

export default Expenses;