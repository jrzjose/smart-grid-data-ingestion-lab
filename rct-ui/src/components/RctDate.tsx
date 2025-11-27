import * as React from 'react';

const RctDate: React.FC<DateTimeProps> = ({ timestamp }) => {
  const dateObject = new Date(timestamp); 

  // Format the date using built-in methods
  const formattedDate = dateObject.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  }); // Example: "March 15, 2023"

  const formattedTime = dateObject.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: 'numeric',
    hour12: true,
  }); // Example: "12:00:00 AM"

  return (
    <p>
      {formattedDate} - : {formattedTime}
    </p>
  );
};

export default RctDate;