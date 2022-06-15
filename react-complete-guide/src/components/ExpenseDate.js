import './ExpenseDate.css';

function ExpenseDate(props) {
  const year = props.date.getFullYear();
  const month = props.date.toLocaleString('pt-BR', { month: 'long' });
  const day = props.date.toLocaleString('pt-BR', { day: '2-digit' });

  return (
    <div className='expense-date'>
      <div className='expense-date__month'>{ month }</div>
      <div className='expense-date__year'>{ year }</div>
      <div className='expense-date__day'>{ day }</div>
    </div>
  );
}

export default ExpenseDate;