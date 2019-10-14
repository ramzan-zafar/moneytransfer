package com.moneytransfer;

import static spark.Spark.before;
import static spark.Spark.exception;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.Gson;
import com.moneytransfer.controller.AccountController;
import com.moneytransfer.controller.UserController;
import com.moneytransfer.dao.DAOFactory;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.NotSufficientBalanceException;
import com.moneytransfer.exception.SameAccountException;
import com.moneytransfer.exception.UserException;
import com.moneytransfer.response.StandardResponse;
import com.moneytransfer.response.StatusResponse;

import spark.Spark;

public class MoneyTransferAPI {
    public static void main(String[] args) {
        startSparkApplication();
    }

    private static void startSparkApplication() {
    		Spark.port(8088);
        setExceptionHandlers();
        setResponseType();
        UserController userController = new UserController();
        userController.registerUserApiRoutes();
        AccountController accountController = new AccountController();
        accountController.registerAccountApiRoutes();
        DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
		h2DaoFactory.populateTestData();
    }

    private static void setResponseType() {
        before((req, res) -> {
            res.type("application/json");
        });
    }

    private static void setExceptionHandlers() {
        exception(NotFoundException.class, (e, req, res) -> {
            res.status(HttpStatus.BAD_REQUEST_400);
            res.body(new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, e.getMessage())));
        });
        exception(AlreadyExistException.class, (e, req, res) -> {
            res.status(HttpStatus.CONFLICT_409);
            res.body(new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, e.getMessage())));
        });
        exception(NotSufficientBalanceException.class, (e, req, res) -> {
            res.status(HttpStatus.BAD_REQUEST_400);
            res.body(new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, e.getMessage())));
        });
        exception(SameAccountException.class, (e, req, res) -> {
            res.status(HttpStatus.BAD_REQUEST_400);
            res.body(new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, e.getMessage())));
        });
        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(HttpStatus.BAD_REQUEST_400);
            res.body(new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, "Illegal arguments provided in request")));
        });
        exception(NullPointerException.class, (e, req, res) -> {
            res.status(HttpStatus.BAD_REQUEST_400);
            res.body(new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, "Insufficient arguments provided")));
        });
        exception(Exception.class, (e, req, res) -> {
            res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            res.body(new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, e.getMessage())));
        });
        exception(UserException.class, (e, req, res) -> {
            res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            res.body(new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, e.getMessage())));
        });
    }
}
