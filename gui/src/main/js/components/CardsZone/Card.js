import React from "react"
import {func, object, string} from "prop-types"

const Card = ({card, title = "", onClick= ()=>{}, classNames = ""}) => (
    <div className={classNames}
          onClick={onClick}>
        <img src={`http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=${card.multiverseid}&type=card`}
             alt={card.name}/>
    </div>
);


Card.propTypes = {
    card: object.isRequired,
    onClick: func,
    title: string,
    classNames: string
};

export default Card;

