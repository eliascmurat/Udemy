import React from 'react';

import './Expenses.css';

import Card from '../UI/Card';
import ExpenseItem from './ExpenseItem';

const Expenses = (props) => {  
  return (
    <Card className='expenses'>
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
    </Card>
  );
}

export default Expenses;